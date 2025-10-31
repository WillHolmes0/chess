package dataaccess;

import chess.ChessGame;
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
}
