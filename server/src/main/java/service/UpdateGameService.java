package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import requests.UpdateGameRequest;
import responses.CreateGameResponse;
import responses.UpdateGameResponse;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

public class UpdateGameService {
    MemoryDatabase memoryDatabase;

    public UpdateGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public UpdateGameResponse updateGame(UpdateGameRequest updateGameRequest) throws DataAccessException, UnauthorizedException, BadRequestException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        if (updateGameRequest.gameData() == null || updateGameRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        AuthData authData = authDAO.getAuthData(updateGameRequest.authorization());
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        gameDAO.updateGame(updateGameRequest.gameData());
        return new UpdateGameResponse(updateGameRequest.gameData());
    }
}
