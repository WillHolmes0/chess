package server.service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryDatabase;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.requests.CreateGameRequest;
import model.responses.CreateGameResponse;

import java.util.Random;

public class CreateGameService {
    private MemoryDatabase memoryDatabase;

    public CreateGameService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        if (createGameRequest.gameName() == null || createGameRequest.authorization() == null) {
            throw new BadRequestException("Error: bad request");
        }
        AuthDAO authDAO = new AuthDAO(memoryDatabase);
        GameDAO gameDAO = new GameDAO(memoryDatabase);
        AuthData authData = authDAO.getAuthData(createGameRequest.authorization());
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = new GameData(generateGameId(), null, null, createGameRequest.gameName(), new ChessGame());
        gameDAO.createGame(gameData);
        return new CreateGameResponse(gameData.gameId());
    }

    public int generateGameId() {
        Random randomGenerator = new Random();
        int gameId = randomGenerator.nextInt(1, 10000);
        System.out.println(gameId);
        GameDAO gameDAO = new GameDAO(memoryDatabase);
        if (gameDAO.getGame(gameId) == null) {
            return gameId;
        }
        return generateGameId();
    }
}
