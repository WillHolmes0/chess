package server;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import handlers.RemovePlayerHandler;
import io.javalin.websocket.*;
import requests.RemovePlayerRequest;
import requests.UpdateGameRequest;
import responses.RemovePlayerResponse;
import responses.UpdateGameResponse;
import service.RemovePlayerService;
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
                case UserGameCommand.CommandType.LEAVE -> serverMessage = removePlayerHandler(userGameCommand);
                case UserGameCommand.CommandType.MAKE_MOVE -> serverMessage = makeMoveHandler(ctx);
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
}
