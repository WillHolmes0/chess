package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exception.BadRequestException;
import service.requests.CreateGameRequest;
import service.responses.CreateGameResponse;

public class CreateGameTests {

    @BeforeEach
    public void init() {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        ClearApplicationService clearApplicationService = new ClearApplicationService(memoryDatabase);
        try {
            clearApplicationService.clearAll();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void createGameSucess() {
//        MemoryDatabase memoryDatabase = new MemoryDatabase();
//        memoryDatabase.authTokens().put("bypass", new AuthData("bypass", "user"));
//        String authToken = "bypass";
//        CreateGameRequest createGameRequestOne = new CreateGameRequest("game1", authToken);
//        CreateGameRequest createGameRequestTwo = new CreateGameRequest("game2", authToken);
//        CreateGameService createGameService = new CreateGameService(memoryDatabase);
//        try {
//            createGameService.createGame(createGameRequestOne);
//            createGameService.createGame(createGameRequestTwo);
//        } catch (Exception e) {}
//        Assertions.assertEquals(2, memoryDatabase.games().size());

        //Database Version
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        try {
            DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();
            DatabaseGameDAO databaseGameDAO = new DatabaseGameDAO();
            CreateGameService createGameService = new CreateGameService(memoryDatabase);
            String authToken = "bypass";
            databaseAuthDAO.addAuthToken(new AuthData(authToken, "user"));
            CreateGameRequest createGameRequestOne = new CreateGameRequest("game1", authToken);
            CreateGameResponse result = createGameService.createGame(createGameRequestOne);
            GameData resultGameData = databaseGameDAO.getGame(result.gameID());
            Assertions.assertEquals("game1", resultGameData.gameName());
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
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
