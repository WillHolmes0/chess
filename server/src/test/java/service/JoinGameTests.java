package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import dataaccess.MemoryDatabase;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import service.exception.AlreadyTakenException;
import requests.JoinGameRequest;

public class JoinGameTests {
    private String authToken = "bypass";
    private MemoryDatabase memoryDatabase;

    @BeforeEach()
    public void setup() {
        memoryDatabase = new MemoryDatabase();
        memoryDatabase.authTokens().put("bypass", new AuthData("bypass", "user6"));
        ClearApplicationService clearApplicationService = new ClearApplicationService(memoryDatabase);
        try {
            clearApplicationService.clearAll();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void joinGameSucess() {
        memoryDatabase.games().put("1", new GameData(1, "user6", null, "game1", new ChessGame()));

        JoinGameService joinGameService = new JoinGameService(memoryDatabase);
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1, "bypass");
        try {
            joinGameService.joinGame(joinGameRequest);
        } catch (Exception e) {}

        GameData game = memoryDatabase.games().get("1");
        Assertions.assertEquals(1, memoryDatabase.games().size());
        Assertions.assertEquals("user6", game.whiteUsername());
        Assertions.assertNull(game.blackUsername());

    }

    @Test
    public void databaseJoinGameSucess() {
        try {
            DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();
            DatabaseGameDAO databaseGameDAO = new DatabaseGameDAO();
            int gameID = 55;
            ChessGame chessGame = new ChessGame();
            GameData gameData = new GameData(gameID, null, null, "game5", chessGame);
            databaseGameDAO.createGame(gameData);
            String authToken = databaseAuthDAO.generateAuthToken();
            databaseAuthDAO.addAuthToken(new AuthData(authToken, "user"));

            JoinGameService joinGameService = new JoinGameService(memoryDatabase);
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1, "bypass");

            GameData resultGameData = databaseGameDAO.getGame(gameID);
            Assertions.assertEquals("game5", resultGameData.gameName());

        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }

    }


//    @Test
//    public void colorTakenException() {
//        memoryDatabase.games().put("1", new GameData(1, "user7", null, "game1", new ChessGame()));
//        try {
//            DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();
//            databaseAuthDAO.addAuthToken(new AuthData("bypass", "user"));
//        } catch (DataAccessException e) {
//            Assertions.fail(e.getMessage());
//        }
//
//        JoinGameService joinGameService = new JoinGameService(memoryDatabase);
//        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1, "bypass");
//
//        Assertions.assertThrows(AlreadyTakenException.class, () -> joinGameService.joinGame(joinGameRequest));
//    }

    @Test
    public void colorTakenExceptionDatabase() {
        String authToken;
        int gameID;
        try {
            DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();
            DatabaseGameDAO databaseGameDAO = new DatabaseGameDAO();

            gameID = 55;
            ChessGame chessGame = new ChessGame();
            GameData gameData = new GameData(gameID, "solo", null, "game5", chessGame);
            databaseGameDAO.createGame(gameData);

            authToken = databaseAuthDAO.generateAuthToken();
            databaseAuthDAO.addAuthToken(new AuthData(authToken, "user"));

            JoinGameService joinGameService = new JoinGameService(memoryDatabase);
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID, authToken);

            Assertions.assertThrows(AlreadyTakenException.class, () -> joinGameService.joinGame(joinGameRequest));
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }


}
