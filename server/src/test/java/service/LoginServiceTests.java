package service;

import dataaccess.MemoryDatabase;
import model.UserData;
import org.junit.jupiter.api.*;
import service.exception.UnauthorizedException;
import service.requests.LoginRequest;
import service.responses.LoginResponse;

public class LoginServiceTests {
    private String authToken;
    private MemoryDatabase memoryDatabase;

    @BeforeEach
    public void setup() {
        memoryDatabase = new MemoryDatabase();
        memoryDatabase.users().put("user1", new UserData("user1", "user1password", "user1email"));
        memoryDatabase.users().put("user2", new UserData("user2", "user2password", "user2email"));
    }

    @Test
    public void LoginSucess() {
        LoginRequest loginRequest = new LoginRequest("user1", "user1password");
        LoginService loginService = new LoginService(memoryDatabase);
        try {
            LoginResponse loginResponse = loginService.loginUser(loginRequest);
            Assertions.assertEquals(String.class, loginResponse.authToken().getClass());
            Assertions.assertEquals("user1", loginResponse.username());
        } catch (Exception e) {}
    }

    @Test
    public void LoginBadPasswordFailure() {
        LoginRequest loginRequest = new LoginRequest("user2", "user1password");
        LoginService loginService = new LoginService(memoryDatabase);
        Assertions.assertThrows(UnauthorizedException.class, () -> loginService.loginUser(loginRequest));
    }


}
