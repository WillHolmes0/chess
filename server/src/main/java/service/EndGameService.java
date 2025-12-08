package service;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import requests.EndGameRequest;
import responses.EndGameResponse;
import responses.UpdateGameResponse;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

public class EndGameService {
    MemoryDatabase memoryDatabase;

    public EndGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public EndGameResponse endGame(EndGameRequest endGameRequest) throws DataAccessException, UnauthorizedException, BadRequestException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        if (endGameRequest.gameID() == 0 || endGameRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        AuthData authData = authDAO.getAuthData(endGameRequest.authorization());
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = gameDAO.getGame(endGameRequest.gameID());
        if (!gameData.game().isActive()) {
            throw new BadRequestException("Error: you cannot resign the game as it is no longer being played");
        }
        String color;
        if (gameData.whiteUsername().equals(authData.username())) {
            color = "white";
        } else if (gameData.blackUsername().equals(authData.username())) {
            color = "black";
        } else {
            throw new UnauthorizedException("Error: the user is not a player in the game");
        }
        gameData.game().endGame();
        gameDAO.updateGame(gameData);
        return new EndGameResponse(authData.username(), color);
    }
}
