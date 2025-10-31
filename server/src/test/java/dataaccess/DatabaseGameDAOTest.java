package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        } catch (DataAccessException e) {
            Assertions.fail("threw DataAccessException when adding new game");
        }
        Assertions.assertDoesNotThrow(() -> {databaseGameDAO.clearDatabase();});
        Assertions.assertThrows(DataAccessException.class, () -> {databaseGameDAO.getGame(gameID);});
    }
}
