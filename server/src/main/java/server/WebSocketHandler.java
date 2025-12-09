package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import requests.*;
import responses.*;
import service.*;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;
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

    private void broadcastAll(String gameID, ServerMessage serverMessage, Session exclusion) throws IOException {
        for (var session : gameClients.get(gameID)) {
            System.out.println("session");
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
            broadcastAll(String.valueOf(userGameCommand.getGameID()), notificationMessage, ctx.session);

            LoadGameMessage loadGameMessage = new LoadGameMessage(retrievePlayerGameResponse.chessGame());
            broadcastSelf(String.valueOf(userGameCommand.getGameID()), loadGameMessage, ctx.session);
        } catch (DataAccessException | UnauthorizedException | BadRequestException | NoMatchException e) {
            errorHandler(userGameCommand.getGameID(), e.getMessage(), ctx);
        }
    }

    public void removePlayerHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws IOException {
        try {
            RemovePlayerService removePlayerService = new RemovePlayerService(memoryDatabase);
            RemovePlayerRequest removePlayerRequest = new RemovePlayerRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
            RemovePlayerResponse removePlayerResponse = removePlayerService.removePlayer(removePlayerRequest);
            ServerMessage serverMessage = new NotificationMessage(String.format("%s %s left the game", removePlayerResponse.color(), removePlayerResponse.username()));
            broadcastAll(String.valueOf(userGameCommand.getGameID()), serverMessage, ctx.session);
            removeSessionFromGame(ctx, userGameCommand);
        } catch (DataAccessException | UnauthorizedException | NoMatchException | BadRequestException e) {
            errorHandler(userGameCommand.getGameID(), e.getMessage(), ctx);
        }
    }

    public void makeMoveHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws IOException {
        try {
            MakeMoveCommand makeMoveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
            UpdateGameService updateGameService = new UpdateGameService(memoryDatabase);
            UpdateGameRequest updateGameRequest = new UpdateGameRequest(makeMoveCommand.getChessMove(), makeMoveCommand.getGameID(), makeMoveCommand.getAuthToken());
            UpdateGameResponse updateGameResponse = updateGameService.updateGame(updateGameRequest);

            ServerMessage serverMessage = new LoadGameMessage(updateGameResponse.gameData().game());
            broadcastAll(String.valueOf(userGameCommand.getGameID()), serverMessage, null);

            NotificationMessage notificationMessage = new NotificationMessage(
                String.format("%s player moved %s from %s to %s",
                updateGameResponse.username(),
                updateGameResponse.chessPiece(),
                updateGameResponse.startPosition(),
                updateGameResponse.endPosition()
                ));
            broadcastAll(String.valueOf(userGameCommand.getGameID()), notificationMessage, ctx.session);

            handleCheck(updateGameResponse.gameData(), ctx);

        } catch (DataAccessException | BadRequestException | UnauthorizedException e) {
            errorHandler(userGameCommand.getGameID(), e.getMessage(), ctx);
        }
    }

    public void endGameHandler(UserGameCommand userGameCommand, WsMessageContext ctx) throws IOException {
        try {
            EndGameService endGameService = new EndGameService(memoryDatabase);
            EndGameRequest endGameRequest = new EndGameRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
            EndGameResponse endGameResponse = endGameService.endGame(endGameRequest);
            ServerMessage serverMessage = new NotificationMessage(String.format("%s player %s resigned the game", endGameResponse.color(), endGameResponse.username()));
            broadcastAll(String.valueOf(userGameCommand.getGameID()), serverMessage, null);
        } catch (DataAccessException | UnauthorizedException | BadRequestException e) {
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

    private String getPlayerRoleString(String role) throws NoMatchException {
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
        if (message.toLowerCase().contains("error:")) {
            message = message.split(":")[1].strip();
        }
        broadcastSelf(gameIDString, new ErrorMessage(message), ctx.session);
    }

    private void handleCheck(GameData gameData, WsMessageContext ctx) throws IOException {
        String checkmateMessage = getPlayerInCheckmate(gameData);
        if (checkmateMessage != null) {
            NotificationMessage notificationMessage = new NotificationMessage(checkmateMessage);
            broadcastAll(String.valueOf(gameData.gameID()), notificationMessage, null);
        } else {
            String checkMessage = getPlayerInCheck(gameData);
            if (checkMessage != null) {
                NotificationMessage notificationMessage = new NotificationMessage(checkMessage);
                broadcastAll(String.valueOf(gameData.gameID()), notificationMessage, null);
            }
        }

    }

    private String getPlayerInCheck(GameData gameData) {
        ChessGame chessGame = gameData.game();
        if(chessGame.isInCheck(ChessGame.TeamColor.WHITE)) {
            return String.format("white player %s is in check", gameData.whiteUsername());
        }
        if (chessGame.isInCheck(ChessGame.TeamColor.BLACK)) {
            return String.format("black player %s is in check", gameData.blackUsername());
        }
        return null;
    }

    private String getPlayerInCheckmate(GameData gameData) {
        ChessGame chessGame = gameData.game();
        if(chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            return String.format("white player %s is in checkmate", gameData.whiteUsername());
        }
        if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            return String.format("black player %s is in checkmate", gameData.blackUsername());
        }
        return null;
    }

}

