package service;

import chess.InvalidMoveException;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import requests.RetrieveUserRequest;
import responses.RetrieveUserResponse;
import responses.UpdateGameResponse;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;

public class RetrieveUserService {
    MemoryDatabase memoryDatabase;

    public RetrieveUserService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public RetrieveUserResponse retrieveUser(RetrieveUserRequest retrieveUserRequest) throws DataAccessException, UnauthorizedException, BadRequestException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        if (retrieveUserRequest.gameID() == 0 || retrieveUserRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        AuthData authData = authDAO.getAuthData(retrieveUserRequest.authorization());
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = gameDAO.getGame(retrieveUserRequest.gameID());
        String username = authData.username();
        String color;
        if (gameData.whiteUsername().equals(username)) {
            color = "white";
        } else if (gameData.blackUsername().equals(username)) {
            color = "black";
        } else {
            color = "observer";
        }
        return new RetrieveUserResponse(username, color);
    }
}
