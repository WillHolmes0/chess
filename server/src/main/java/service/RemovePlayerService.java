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
            gameDAO.setPlayer(null, playerColor, removePlayerRequest.gameID());

            String playerColorString = (playerColor == ChessGame.TeamColor.WHITE) ? "white" : "black";
            return new RemovePlayerResponse(playerColorString, username);
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    private ChessGame.TeamColor getUsernameColorFromGame(String username, GameData gameData) throws NoMatchException {
        if (username.equals(gameData.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        } else {
            throw new NoMatchException("Error: The user is not listed as either color");
        }
    }
}
