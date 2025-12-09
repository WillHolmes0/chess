package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import requests.JoinGameRequest;
import requests.RemovePlayerRequest;
import responses.JoinGameResponse;
import responses.RemovePlayerResponse;
import server.ResponseException;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;
import websocket.NoMatchException;

import javax.xml.crypto.Data;

public class RemovePlayerService {
    MemoryDatabase memoryDatabase;

    public RemovePlayerService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public RemovePlayerResponse removePlayer(RemovePlayerRequest removePlayerRequest) throws UnauthorizedException, BadRequestException, DataAccessException, NoMatchException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        if (removePlayerRequest.gameID() == 0 || removePlayerRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        AuthData authData = authDAO.getAuthData(removePlayerRequest.authorization());
        GameData gameData = gameDAO.getGame(removePlayerRequest.gameID());

        if (authData != null) {
            String username = authData.username();
            ChessGame.TeamColor playerColor = getUsernameColorFromGame(username, gameData);
            if (playerColor != null) {
                gameDAO.setPlayer(null, playerColor, removePlayerRequest.gameID());
            }

            return new RemovePlayerResponse(getPlayerType(playerColor), username);
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    private ChessGame.TeamColor getUsernameColorFromGame(String username, GameData gameData) throws NoMatchException {
        if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
            return ChessGame.TeamColor.WHITE;
        } else if (gameData.blackUsername() != null && username.equals(gameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        return null;
    }

    private String getPlayerType(ChessGame.TeamColor teamColor) {
        if (teamColor == null) {
            return "observer";
        } else if (teamColor == ChessGame.TeamColor.WHITE) {
            return "white player";
        } else {
            return "black player";
        }
    }

//    private void removeIfWhite(String username, GameData gameData, GameDAO gameDAO, RemovePlayerRequest removePlayerRequest) throws NoMatchException, DataAccessException {
//        if (gameData.whiteUsername() != null) {
//            if (username.equals(gameData.whiteUsername())) {
//                gameDAO.setPlayer(null, ChessGame.TeamColor.WHITE, removePlayerRequest.gameID());
//            }
//        }
//    }
//
//    private void removeIfWhite(String username, GameData gameData, GameDAO gameDAO, RemovePlayerRequest removePlayerRequest) throws NoMatchException, DataAccessException {
//        if (gameData.whiteUsername() != null) {
//            if (username.equals(gameData.whiteUsername())) {
//                gameDAO.setPlayer(null, ChessGame.TeamColor.WHITE, removePlayerRequest.gameID());
//            }
//        }
//    }
}
