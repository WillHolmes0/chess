package service;

import chess.ChessGame;
import dataaccess.MemoryDatabase;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.exception.BadRequestException;
import server.requests.CreateGameRequest;

public class CreateGameTests {

    @Test
    public void createGameSucess() {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.authTokens().put("bypass", new AuthData("bypass", "user"));
        String authToken = "bypass";
        CreateGameRequest createGameRequestOne = new CreateGameRequest("game1", authToken);
        CreateGameRequest createGameRequestTwo = new CreateGameRequest("game2", authToken);
        CreateGameService createGameService = new CreateGameService(memoryDatabase);
        try {
            createGameService.createGame(createGameRequestOne);
            createGameService.createGame(createGameRequestTwo);
        } catch (Exception e) {}
        Assertions.assertEquals(2, memoryDatabase.games().size());
    }

    @Test
    public void createGameFailure() {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.authTokens().put("bypass", new AuthData("bypass", "user"));
        String authToken = "bypass";
        CreateGameService createGameService = new CreateGameService(memoryDatabase);
        CreateGameRequest createGameRequestOne = new CreateGameRequest(null, authToken);
        Assertions.assertThrows(BadRequestException.class, () -> createGameService.createGame(createGameRequestOne));
    }
}
