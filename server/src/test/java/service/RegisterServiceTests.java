package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseUserDAO;
import dataaccess.MemoryDatabase;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exception.AlreadyTakenException;
import service.requests.RegisterRequest;
import service.responses.RegisterResponse;

public class RegisterServiceTests {
    private MemoryDatabase memoryDatabase;
    private RegisterService registerService;

    @BeforeEach
    public void setup() {
        memoryDatabase = new MemoryDatabase();
        registerService = new RegisterService(memoryDatabase);
        ClearApplicationService clearApplicationService = new ClearApplicationService(memoryDatabase);
        try {
            clearApplicationService.clearAll();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void registerSucess() {
        RegisterRequest registerRequest = new RegisterRequest("user1", "password1", "email1");
        try {
            RegisterResponse registerResponse = registerService.registerUser(registerRequest);
            Assertions.assertEquals("user1", memoryDatabase.users().get("user1").username());
            Assertions.assertEquals("password1", memoryDatabase.users().get("user1").password());
            Assertions.assertEquals("email1", memoryDatabase.users().get("user1").email());
            Assertions.assertEquals(String.class, registerResponse.authToken().getClass());
            Assertions.assertEquals("user1", registerResponse.username());
            Assertions.assertEquals(1, memoryDatabase.authTokens().size());
        } catch (Exception e) {}
    }

//    @Test
//    public void UsernameTakenFailure() {
//        memoryDatabase.users().put("user1", new UserData("user1", "differentpassword", "differentemail"));
//        RegisterRequest registerRequest = new RegisterRequest("user1", "password1", "email1");
//        Assertions.assertThrows(AlreadyTakenException.class, () -> registerService.registerUser(registerRequest));
//    }

    @Test
    public void registerUserFailureDatabase() {
        try {
            UserDAO userDAO = new DatabaseUserDAO();
            userDAO.addUser(new UserData("willh", "sdfd", "sfas"));
            Assertions.assertThrows(AlreadyTakenException.class, () -> {registerService.registerUser(new RegisterRequest("willh", "sdaf", "dsf"));});
        } catch (DataAccessException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
