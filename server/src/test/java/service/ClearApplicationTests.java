package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;

public class ClearApplicationTests {

    @Test
    public void clearAllSuccess() {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        DatabaseUserDAO databaseUserDAO;
        DatabaseGameDAO databaseGameDAO;
        DatabaseAuthDAO databaseAuthDAO;
        String authToken;
        String username = "will";
        int gameID = 1234;
        ClearApplicationService clearApplicationService = new ClearApplicationService(memoryDatabase);
        try {
            clearApplicationService.clearAll();
            databaseUserDAO = new DatabaseUserDAO();
            databaseGameDAO = new DatabaseGameDAO();
            databaseAuthDAO = new DatabaseAuthDAO();

            authToken = databaseAuthDAO.generateAuthToken();
            databaseAuthDAO.addAuthToken(new AuthData(authToken, "user"));

            databaseUserDAO.addUser(new UserData(username, "sdf", "dfs"));

            ChessGame chessGame = new ChessGame();
            GameData gameData = new GameData(gameID, null, null, "game", chessGame);
            databaseGameDAO.createGame(gameData);

            clearApplicationService.clearAll();

            //recreate tables so they can be checked if they are empty

            databaseUserDAO = new DatabaseUserDAO();
            databaseGameDAO = new DatabaseGameDAO();
            databaseAuthDAO = new DatabaseAuthDAO();

            UserData userDataResult = databaseUserDAO.getUser(username);
            AuthData authDataResult = databaseAuthDAO.getAuthData(authToken);
            GameData gameDataResult = databaseGameDAO.getGame(gameID);

            Assertions.assertEquals(null, userDataResult);
            Assertions.assertEquals(null, authDataResult);
            Assertions.assertEquals(null, gameDataResult);
        } catch (Exception e) {
            Assertions.fail("threw unexpected Exception");
        }
    }


//    @Test
//    public void clearAllSucess() {
//        MemoryDatabase memoryDatabase = new MemoryDatabase();
//        memoryDatabase.games().put("game1", new GameData(0001, "user1", "user2", "game1", new ChessGame()));
//        memoryDatabase.games().put("game2", new GameData(3000, "user1", "user2", "game2", new ChessGame()));
//        memoryDatabase.games().put("game3", new GameData(26, "user1", "user2", "game3", new ChessGame()));
//
//        memoryDatabase.authTokens().put("user1", new AuthData("auth1", "user1"));
//        memoryDatabase.authTokens().put("user2", new AuthData("auth2", "user2"));
//        memoryDatabase.authTokens().put("user3", new AuthData("auth3", "user3"));
//
//        memoryDatabase.users().put("user1", new UserData("user1", "password1", "email1"));
//        memoryDatabase.users().put("user2", new UserData("user2", "password2", "email2"));
//        memoryDatabase.users().put("user3", new UserData("user3", "password3", "email3"));
//
//        ClearApplicationService clearApplicationService = new ClearApplicationService(memoryDatabase);
//        try {
//            clearApplicationService.clearAll();
//        } catch (Exception e) {
//
//        }
//        Assertions.assertEquals(0, memoryDatabase.users().size());
//        Assertions.assertEquals(0, memoryDatabase.games().size());
//        Assertions.assertEquals(0, memoryDatabase.authTokens().size());
//    }


}
