package service;

import chess.ChessGame;
import dataaccess.*;
import service.exception.BadRequestException;
import service.exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import service.requests.CreateGameRequest;
import service.responses.CreateGameResponse;

import java.util.Random;

public class CreateGameService {
    private MemoryDatabase memoryDatabase;

    public CreateGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws DataAccessException, UnauthorizedException, BadRequestException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        if (createGameRequest.gameName() == null || createGameRequest.authorization() == null) {
            throw new BadRequestException("Error: missing field");
        }
        AuthData authData = authDAO.getAuthData(createGameRequest.authorization());
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = new GameData(generateGameId(), null, null, createGameRequest.gameName(), new ChessGame());
        gameDAO.createGame(gameData);
        return new CreateGameResponse(gameData.gameID());
    }

    public int generateGameId() {
        Random randomGenerator = new Random();
        int gameId = randomGenerator.nextInt(1, 10000);
        System.out.println(gameId);
        MemoryGameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        if (gameDAO.getGame(gameId) == null) {
            return gameId;
        }
        return generateGameId();
    }
}
