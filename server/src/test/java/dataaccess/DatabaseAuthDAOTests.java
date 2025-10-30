package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseAuthDAOTests {
    private static DatabaseAuthDAO databaseAuthDAO;

    @BeforeEach
    public void setup() {
        try {
            databaseAuthDAO = new DatabaseAuthDAO();
            databaseAuthDAO.clearDatabase();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void addAuthTokenSucess() {
        Assertions.assertDoesNotThrow(() -> {databaseAuthDAO.addAuthToken(new AuthData("test auth token", "test user"));});
    }


    @Test
    public void getAuthTokenSucess() {
        String authToken = databaseAuthDAO.generateAuthToken();
        AuthData startingAuthData = new AuthData(authToken, "test user");
        try {
            databaseAuthDAO.addAuthToken(startingAuthData);
            AuthData results = databaseAuthDAO.getAuthData(authToken);
            Assertions.assertEquals(startingAuthData.authToken(), results.authToken());
            Assertions.assertEquals(startingAuthData.username(), results.username());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            Assertions.fail("threw DataAccessException");
        }

    }

    @Test
    public void generateAuthTokenSucess() {
        String authToken = databaseAuthDAO.generateAuthToken();
        Assertions.assertEquals(36, authToken.length());
    }

    @Test
    public void clearDatabaseTest() {
        String authToken = databaseAuthDAO.generateAuthToken();
        try {
            databaseAuthDAO.addAuthToken(new AuthData(authToken, "user1"));
        } catch (DataAccessException e) {
            Assertions.fail("threw DataAccessException when adding the authToken");
        }
        Assertions.assertDoesNotThrow(() -> {databaseAuthDAO.clearDatabase();});
        Assertions.assertThrows(DataAccessException.class, () -> {databaseAuthDAO.getAuthData(authToken);});
    }

}
