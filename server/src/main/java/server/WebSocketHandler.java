package server;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import handlers.RemovePlayerHandler;
import io.javalin.websocket.*;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
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
import java.util.ArrayList;
import java.util.HashMap;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    MemoryDatabase memoryDatabase = new MemoryDatabase();
    HashMap<String, ArrayList<Session>> gameClients;


    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Connected to websocket");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
//            ServerMessage serverMessage;
            System.out.println(userGameCommand.getCommandType());
            switch (userGameCommand.getCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> connectHandler(userGameCommand, ctx);
                case UserGameCommand.CommandType.LEAVE -> removePlayerHandler(userGameCommand, ctx);
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMoveHandler(userGameCommand, ctx);
                case UserGameCommand.CommandType.RESIGN -> endGameHandler(userGameCommand, ctx);
                default -> throw new NoMatchException("Error: could get a valid command type from the UserGameCommand");
            }
//            String gameID = String.valueOf(userGameCommand.getGameID());
//          ctx.session.getRemote().sendString(new Gson().toJson(serverMessage));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void broadcast(String gameID, ServerMessage serverMessage, Session exclusion) throws IOException {
        for (var session : gameClients.get(gameID)) {
            if (session != exclusion) {
                session.getRemote().sendString(new Gson().toJson(serverMessage));
            }
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("websocket closed");
    }

    public void connectHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws DataAccessException, IOException {
        addSessionToGame(ctx, userGameCommand);
        RetrieveUserService retrieveUserService = new RetrieveUserService(memoryDatabase);
        RetrieveUserRequest retrieveUserRequest = new RetrieveUserRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
        RetrieveUserResponse retrieveUserResponse = retrieveUserService.retrieveUser(retrieveUserRequest);
        String playerType = getPlayerType(retrieveUserResponse.color());
        ServerMessage serverMessage = new NotificationMessage(String.format("%s joined the game as %s", retrieveUserResponse.username(), playerType));
        broadcast(String.valueOf(userGameCommand.getGameID()), serverMessage, ctx.session);
    }

    public void removePlayerHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws DataAccessException, IOException {
        removeSessionFromGame(ctx, userGameCommand);
        RemovePlayerService removePlayerService = new RemovePlayerService(memoryDatabase);
        RemovePlayerRequest removePlayerRequest = new RemovePlayerRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
        RemovePlayerResponse removePlayerResponse = removePlayerService.removePlayer(removePlayerRequest);
        ServerMessage serverMessage = new NotificationMessage(String.format("%s, player %s left the game", removePlayerResponse.color(), removePlayerResponse.username()));
        broadcast(String.valueOf(userGameCommand.getGameID()), serverMessage, ctx.session);
    }

    public void makeMoveHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws DataAccessException, IOException {
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
        UpdateGameService updateGameService = new UpdateGameService(memoryDatabase);
        UpdateGameRequest updateGameRequest = new UpdateGameRequest(makeMoveCommand.getChessMove(), makeMoveCommand.getGameID(), makeMoveCommand.getAuthToken());
        UpdateGameResponse updateGameResponse = updateGameService.updateGame(updateGameRequest);
        ServerMessage serverMessage = new LoadGameMessage(updateGameResponse.chessGame());
        broadcast(String.valueOf(userGameCommand.getGameID()), serverMessage, ctx.session);
    }

    public void endGameHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws DataAccessException, IOException {
        EndGameService endGameService = new EndGameService(memoryDatabase);
        EndGameRequest endGameRequest = new EndGameRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
        EndGameResponse endGameResponse = endGameService.endGame(endGameRequest);
        ServerMessage serverMessage = new NotificationMessage(String.format("%s player %s resigned the game", endGameResponse.color(), endGameResponse.username()));
        broadcast(String.valueOf(userGameCommand.getGameID()), serverMessage, ctx.session);
    }

    private void addSessionToGame(WsMessageContext ctx, UserGameCommand userGameCommand) {
        gameClients.get(String.valueOf(userGameCommand.getGameID())).add(ctx.session);
    }

    private void removeSessionFromGame(WsMessageContext ctx, UserGameCommand userGameCommand) {
        gameClients.get(String.valueOf(userGameCommand.getGameID())).remove(ctx.session);
    }

    private String getPlayerType(String color) {
        return switch (color) {
            case "white" -> "the white player";
            case "black" -> "the black player";
            default -> "an obvserver";
        };
    }
}

