package server;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import handlers.RemovePlayerHandler;
import io.javalin.websocket.*;
import org.eclipse.jetty.server.Authentication;
import requests.EndGameRequest;
import requests.RemovePlayerRequest;
import requests.RetrieveUserRequest;
import requests.UpdateGameRequest;
import responses.EndGameResponse;
import responses.RemovePlayerResponse;
import responses.RetrieveUserResponse;
import responses.UpdateGameResponse;
import service.EndGameService;
import service.RemovePlayerService;
import service.RetrieveUserService;
import service.UpdateGameService;
import websocket.NoMatchException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    MemoryDatabase memoryDatabase = new MemoryDatabase();


    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Connected to websocket");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            ServerMessage serverMessage;
            System.out.println(userGameCommand.getCommandType());
            switch (userGameCommand.getCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> serverMessage = connectHandler(userGameCommand);
                case UserGameCommand.CommandType.LEAVE -> serverMessage = removePlayerHandler(userGameCommand);
                case UserGameCommand.CommandType.MAKE_MOVE -> serverMessage = makeMoveHandler(ctx);
                case UserGameCommand.CommandType.RESIGN -> serverMessage = endGameHandler(userGameCommand);
                default -> throw new NoMatchException("Error: could get a valid command type from the UserGameCommand");
            }
            ctx.session.getRemote().sendString(new Gson().toJson(serverMessage));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("websocket closed");
    }

    public ServerMessage connectHandler(UserGameCommand userGameCommand) throws DataAccessException {
        RetrieveUserService retrieveUserService = new RetrieveUserService(memoryDatabase);
        RetrieveUserRequest retrieveUserRequest = new RetrieveUserRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
        RetrieveUserResponse retrieveUserResponse = retrieveUserService.retrieveUser(retrieveUserRequest);
        String playerType = getPlayerType(retrieveUserResponse.color());
        return new NotificationMessage(String.format("%s joined the game as %s", retrieveUserResponse.username(), playerType));
    }

    public ServerMessage removePlayerHandler(UserGameCommand userGameCommand) throws DataAccessException {
        RemovePlayerService removePlayerService = new RemovePlayerService(memoryDatabase);
        RemovePlayerRequest removePlayerRequest = new RemovePlayerRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
        RemovePlayerResponse removePlayerResponse = removePlayerService.removePlayer(removePlayerRequest);
        return new NotificationMessage(String.format("%s, player %s left the game", removePlayerResponse.color(), removePlayerResponse.username()));
    }

    public ServerMessage makeMoveHandler(WsMessageContext ctx) throws DataAccessException, InvalidMoveException {
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
        UpdateGameService updateGameService = new UpdateGameService(memoryDatabase);
        UpdateGameRequest updateGameRequest = new UpdateGameRequest(makeMoveCommand.getChessMove(), makeMoveCommand.getGameID(), makeMoveCommand.getAuthToken());
        UpdateGameResponse updateGameResponse = updateGameService.updateGame(updateGameRequest);
        return new LoadGameMessage(updateGameResponse.chessGame());
    }

    public ServerMessage endGameHandler(UserGameCommand userGameCommand) throws DataAccessException {
        EndGameService endGameService = new EndGameService(memoryDatabase);
        EndGameRequest endGameRequest = new EndGameRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
        EndGameResponse endGameResponse = endGameService.endGame(endGameRequest);
        return new NotificationMessage(String.format("%s player %s resigned the game", endGameResponse.color(), endGameResponse.username()));
    }

    private String getPlayerType(String color) {
        return switch (color) {
            case "white" -> "the white player";
            case "black" -> "the black player";
            default -> "an obvserver";
        };
    }
}

