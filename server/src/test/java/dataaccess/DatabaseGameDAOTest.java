package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class DatabaseGameDAOTest {
    private static DatabaseGameDAO databaseGameDAO;

    @BeforeEach
    public void setup() {
        try {
            databaseGameDAO = new DatabaseGameDAO();
            databaseGameDAO.clearDatabase();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void setPlayerSucess() {
        int gameID = 3333;
        String newPlayer = "noah";
        ChessGame startingChessGame = new ChessGame();
        GameData startingGameData = new GameData(gameID, "will", null, "whatever", startingChessGame);
        try {
            databaseGameDAO.createGame(startingGameData);
            databaseGameDAO.setPlayer(newPlayer, ChessGame.TeamColor.BLACK, gameID);
            GameData result = databaseGameDAO.getGame(gameID);
            Assertions.assertEquals(startingGameData.gameID(), result.gameID());
            Assertions.assertEquals(startingGameData.whiteUsername(), result.whiteUsername());
            Assertions.assertNotEquals(startingGameData.blackUsername(), result.blackUsername());
            Assertions.assertEquals(newPlayer, result.blackUsername());
            Assertions.assertEquals(startingGameData.gameName(), result.gameName());
            Assertions.assertTrue(startingGameData.game().equals(result.game()));
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void createGameSucess() {
        ChessGame startingChessGame = new ChessGame();
        GameData startingGameData = new GameData(1244, null, "willh", "testGame", startingChessGame);
        Assertions.assertDoesNotThrow(() -> {databaseGameDAO.createGame(startingGameData);});
    }

    @Test
    public void getGameSucess() {
        ChessGame chessGameOne = new ChessGame();
        int gameOneID = 12;
        int gameTwoID = 1219;
        ChessGame chessGameTwo = new ChessGame();
        GameData gameDataOne = new GameData(gameOneID, null, null, "gameone", chessGameOne);
        GameData gameDataTwo = new GameData(gameTwoID, "user1", "user2", "gameTwo", chessGameTwo);
        try {
            databaseGameDAO.createGame(gameDataOne);
            databaseGameDAO.createGame(gameDataTwo);
            GameData result = databaseGameDAO.getGame(gameOneID);
            Assertions.assertEquals(gameDataOne.gameID(), result.gameID());
            Assertions.assertEquals(gameDataOne.whiteUsername(), result.whiteUsername());
            Assertions.assertEquals(gameDataOne.blackUsername(), result.blackUsername());
            Assertions.assertEquals(gameDataOne.gameName(), result.gameName());
            Assertions.assertTrue(result.game().equals(gameDataOne.game()));
        } catch (DataAccessException e) {
            Assertions.fail("threw DataAccessException");
        }
    }

    @Test
    public void clearDatabaseSucess() {
        int gameID = 4444;
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(gameID, null, "willh", "clearablegame", chessGame);
        try {
            databaseGameDAO.createGame(gameData);
            Assertions.assertDoesNotThrow(() -> {databaseGameDAO.clearDatabase();});
            DatabaseGameDAO newdatabaseGameDAO = new DatabaseGameDAO();
            GameData result = newdatabaseGameDAO.getGame(gameID);
            Assertions.assertNull(result);
        } catch (DataAccessException e) {
            Assertions.fail("threw DataAccessException when adding new game");
        }


    }

    @Test
    public void getGameKeysSuccess() {
        int gameIDOne = 4455;
        int gameIDTwo = 78;
        int gameIDThree = 234;
        ChessGame gameOne = new ChessGame();
        ChessGame gameTwo = new ChessGame();
        ChessGame gameThree = new ChessGame();
        GameData gameDataOne = new GameData(gameIDOne, null, null, "gameOne", gameOne);
        GameData gameDataTwo = new GameData(gameIDTwo, null, null, "gameTwo", gameTwo);
        GameData gameDataThree = new GameData(gameIDThree, null, null, "gameOne", gameThree);
        try {
            databaseGameDAO.createGame(gameDataOne);
            databaseGameDAO.createGame(gameDataTwo);
            databaseGameDAO.createGame(gameDataThree);
            HashSet<String> gameKeys = databaseGameDAO.getGameKeys();
            Assertions.assertTrue(gameKeys.contains(String.valueOf(gameIDOne)));
            Assertions.assertTrue(gameKeys.contains(String.valueOf(gameIDTwo)));
            Assertions.assertTrue(gameKeys.contains(String.valueOf(gameIDThree)));
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
