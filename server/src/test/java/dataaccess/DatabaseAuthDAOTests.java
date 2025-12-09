package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

public class DatabaseAuthDAOTests {
    private static DatabaseAuthDAO databaseAuthDAO;

    @BeforeEach
    public void setup() {
        try {
            databaseAuthDAO = new DatabaseAuthDAO();
//            databaseAuthDAO.clearDatabase();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void addAuthTokenSucess() {
        Assertions.assertDoesNotThrow(() -> {databaseAuthDAO.addAuthToken(new AuthData("test auth token", "test user"));});
    }

    @Test
    public void addAuthTokenFailure() {
        try {
            databaseAuthDAO.addAuthToken(new AuthData("test auth token", "test user"));
        } catch (DataAccessException e) {
            Assertions.fail("throw DataAccessException for the first auth input");
        }
        Assertions.assertThrows(DataAccessException.class, () -> {databaseAuthDAO.addAuthToken(new AuthData("test auth token", null));});

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
    public void getAuthTokenFailure() {
        String authToken = databaseAuthDAO.generateAuthToken();
        AuthData startingAuthData = new AuthData(authToken, "test user");
        try {
            databaseAuthDAO.addAuthToken(startingAuthData);
            Assertions.assertEquals(null, databaseAuthDAO.getAuthData("bad auth token"));
        } catch (DataAccessException e) {
            Assertions.fail("threw DataAccessException");
        }
    }

    @Test
    public void clearDatabaseSuccess() {
        String authToken = databaseAuthDAO.generateAuthToken();
        try {
            databaseAuthDAO.addAuthToken(new AuthData(authToken, "user1"));
            Assertions.assertDoesNotThrow(() -> {databaseAuthDAO.clearDatabase();});
            AuthData result = databaseAuthDAO.getAuthData(authToken);
            Assertions.assertEquals(null, result);
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void deleteAuthTokenSucess() {
        String authToken = databaseAuthDAO.generateAuthToken();
        AuthData startingAuthData = new AuthData(authToken, "user1");
        try {
            databaseAuthDAO.addAuthToken(startingAuthData);
            AuthData result = databaseAuthDAO.getAuthData(authToken);
            Assertions.assertEquals(startingAuthData.authToken(), result.authToken());
            Assertions.assertEquals(startingAuthData.username(), result.username());
            databaseAuthDAO.deleteAuthToken(authToken);
            Assertions.assertEquals(null, databaseAuthDAO.getAuthData(authToken));
        } catch (DataAccessException e) {
            Assertions.fail("threw DataAccessException before completely deleting the token");
        }

    }

}
