package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.MemoryDatabase;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.requests.LogoutRequest;
import service.responses.LogoutResponse;

public class LogoutServiceTests {
    private static final Logger log = LoggerFactory.getLogger(LogoutServiceTests.class);
    private MemoryDatabase memoryDatabase;
    private LogoutService logoutService;

    @BeforeEach
    public void setup() {
        memoryDatabase = new MemoryDatabase();
        logoutService = new LogoutService(memoryDatabase);
        ClearApplicationService clearApplicationService = new ClearApplicationService(memoryDatabase);
        try {
            clearApplicationService.clearAll();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

    }

//    @Test
//    public void logoutSucess() {
//        memoryDatabase.authTokens().put("bypass", new AuthData("bypass", "user1"));
//        memoryDatabase.authTokens().put("bypass1", new AuthData("bypass1", "user2"));
//        LogoutRequest logoutRequest = new LogoutRequest("bypass");
//        try {
//            LogoutResponse logoutResponse = logoutService.logoutUser(logoutRequest);
//        } catch (Exception e) {}
//        Assertions.assertEquals(1, memoryDatabase.authTokens().size());
//    }

    @Test
    public void logoutSuccessDatabase() {
        try {
            String authToken = "bypass";
            DatabaseAuthDAO databaseAuthDAO = new DatabaseAuthDAO();
            databaseAuthDAO.addAuthToken(new AuthData(authToken, "user"));
            logoutService.logoutUser(new LogoutRequest(authToken));
            Assertions.assertEquals(null, databaseAuthDAO.getAuthData(authToken));

        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }

}
