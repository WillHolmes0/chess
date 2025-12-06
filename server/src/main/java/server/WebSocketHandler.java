package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import requests.*;
import responses.*;
import service.*;
import websocket.NoMatchException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

//import java.io.IO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    MemoryDatabase memoryDatabase = new MemoryDatabase();
    HashMap<String, ArrayList<Session>> gameClients = new HashMap<>();


    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Connected to websocket");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            System.out.println(userGameCommand.getCommandType());
            switch (userGameCommand.getCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> connectHandler(userGameCommand, ctx);
                case UserGameCommand.CommandType.LEAVE -> removePlayerHandler(userGameCommand, ctx);
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMoveHandler(userGameCommand, ctx);
                case UserGameCommand.CommandType.RESIGN -> endGameHandler(userGameCommand, ctx);
                default -> throw new NoMatchException("Error: could get a valid command type from the UserGameCommand");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void broadcastOthers(String gameID, ServerMessage serverMessage, Session exclusion) throws IOException {
        for (var session : gameClients.get(gameID)) {
            if (session != exclusion) {
                session.getRemote().sendString(new Gson().toJson(serverMessage));
            }
        }
    }

    private void broadcastSelf(String gameID, ServerMessage serverMessage, Session selfSession) throws IOException {
        selfSession.getRemote().sendString(new Gson().toJson(serverMessage));
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("websocket closed");
    }

    public void connectHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws IOException {
        try {
            addSessionToGame(ctx, userGameCommand);
            RetrievePlayerGameService retrievePlayerGameService = new RetrievePlayerGameService(memoryDatabase);
            RetrievePlayerGameRequest retrievePlayerGameRequest = new RetrievePlayerGameRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
            RetrievePlayerGameResponse retrievePlayerGameResponse = retrievePlayerGameService.retrieveData(retrievePlayerGameRequest);

            String playerRole = getPlayerRoleString(retrievePlayerGameResponse.role());
            NotificationMessage notificationMessage = new NotificationMessage(String.format("%s joined the game as %s", retrievePlayerGameResponse.username(), playerRole));
            broadcastOthers(String.valueOf(userGameCommand.getGameID()), notificationMessage, ctx.session);

            LoadGameMessage loadGameMessage = new LoadGameMessage(retrievePlayerGameResponse.chessGame());
            System.out.println(retrievePlayerGameResponse.chessGame());
            broadcastSelf(String.valueOf(userGameCommand.getGameID()), loadGameMessage, ctx.session);
        } catch (DataAccessException e) {
            errorHandler(userGameCommand.getGameID(), e.getMessage(), ctx);
        }
    }

    public void removePlayerHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws IOException {
        try {
            removeSessionFromGame(ctx, userGameCommand);
            RemovePlayerService removePlayerService = new RemovePlayerService(memoryDatabase);
            RemovePlayerRequest removePlayerRequest = new RemovePlayerRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
            RemovePlayerResponse removePlayerResponse = removePlayerService.removePlayer(removePlayerRequest);
            ServerMessage serverMessage = new NotificationMessage(String.format("%s, player %s left the game", removePlayerResponse.color(), removePlayerResponse.username()));
            broadcastOthers(String.valueOf(userGameCommand.getGameID()), serverMessage, ctx.session);
        } catch (DataAccessException e) {
            errorHandler(userGameCommand.getGameID(), e.getMessage(), ctx);
        }
    }

    public void makeMoveHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws IOException {
        try {
            MakeMoveCommand makeMoveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
            UpdateGameService updateGameService = new UpdateGameService(memoryDatabase);
            UpdateGameRequest updateGameRequest = new UpdateGameRequest(makeMoveCommand.getChessMove(), makeMoveCommand.getGameID(), makeMoveCommand.getAuthToken());
            UpdateGameResponse updateGameResponse = updateGameService.updateGame(updateGameRequest);
            ServerMessage serverMessage = new LoadGameMessage(updateGameResponse.chessGame());
            broadcastOthers(String.valueOf(userGameCommand.getGameID()), serverMessage, ctx.session);
        } catch (DataAccessException e) {
            errorHandler(userGameCommand.getGameID(), e.getMessage(), ctx);
        }
    }

    public void endGameHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws DataAccessException, IOException {
        try {
            EndGameService endGameService = new EndGameService(memoryDatabase);
            EndGameRequest endGameRequest = new EndGameRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
            EndGameResponse endGameResponse = endGameService.endGame(endGameRequest);
            ServerMessage serverMessage = new NotificationMessage(String.format("%s player %s resigned the game", endGameResponse.color(), endGameResponse.username()));
            broadcastOthers(String.valueOf(userGameCommand.getGameID()), serverMessage, null);
        } catch (DataAccessException e) {
            errorHandler(userGameCommand.getGameID(), e.getMessage(), ctx);
        }
    }

    private void addSessionToGame(WsMessageContext ctx, UserGameCommand userGameCommand) {
        if (gameClients.get(String.valueOf(userGameCommand.getGameID())) == null) {
            gameClients.put(String.valueOf(userGameCommand.getGameID()), new ArrayList<>());
        }
        gameClients.get(String.valueOf(userGameCommand.getGameID())).add(ctx.session);
    }

    private void removeSessionFromGame(WsMessageContext ctx, UserGameCommand userGameCommand) {
        gameClients.get(String.valueOf(userGameCommand.getGameID())).remove(ctx.session);
    }

    private String getPlayerRoleString(String role) {
        if (role.equals("black")) {
            return "the black player";
        } else if (role.equals("white")) {
            return "the white player";
        } else if (role.equals("observer")) {
            return "an observer";
        }
        throw new NoMatchException("Error: could not retrieve a valid role for the user");
    }

    private void errorHandler(int gameID, String message, WsMessageContext ctx) throws IOException {
        String gameIDString = String.valueOf(gameID);
        broadcastSelf(gameIDString, new ErrorMessage(message), ctx.session);
    }
}

