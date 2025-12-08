package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import requests.RetrievePlayerGameRequest;
import responses.RetrievePlayerGameResponse;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

public class RetrievePlayerGameService {
    MemoryDatabase memoryDatabase;

    public RetrievePlayerGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public RetrievePlayerGameResponse retrieveData(RetrievePlayerGameRequest retrieveGameDataRequest) throws DataAccessException, UnauthorizedException, BadRequestException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        if (retrieveGameDataRequest.gameID() == 0 || retrieveGameDataRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        AuthData authData = authDAO.getAuthData(retrieveGameDataRequest.authorization());
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = gameDAO.getGame(retrieveGameDataRequest.gameID());
        if (gameData == null) {
            throw new BadRequestException("Error: could not get game from given gameID");
        }
        String role = getRole(authData.username(), gameData);
        return new RetrievePlayerGameResponse(gameData.game(), role, authData.username());

    }

    private String getRole(String username, GameData gameData) {
        if (gameData.whiteUsername().equals(username)) {
            return "white";
        } else if (gameData.blackUsername().equals(username)) {
            return "black";
        }
        return "observer";
    }
}
