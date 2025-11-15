package client;

import org.junit.jupiter.api.*;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;
import server.ResponseException;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + String.valueOf(port));
    }

    @BeforeEach
    public void wipeSlate() {
        serverFacade.clearDatabase();
        RegisterRequest registerRequest = new RegisterRequest("willy", "password", "legit@email.com");
        RegisterResponse registerResponse = serverFacade.register(registerRequest);
        authToken = registerResponse.authToken();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTestSucess() {
        RegisterRequest registerRequest = new RegisterRequest("user1", "password", "legit@email.com");
        RegisterResponse response = serverFacade.register(registerRequest);
        Assertions.assertEquals("user1", response.username());
        Assertions.assertNotNull(response.authToken());
    }

    @Test
    public void registerTestFailure() {
        RegisterRequest registerRequest = new RegisterRequest("willy", "password", "legit@email.com");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(registerRequest));
    }

    @Test
    public void loginTestSucess() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        serverFacade.logout(logoutRequest);
        LoginRequest loginRequest = new LoginRequest("willy", "password");
        LoginResponse loginResponse = serverFacade.login(loginRequest);
        Assertions.assertEquals("willy", loginResponse.username());
        Assertions.assertNotNull(loginResponse.authToken());
    }

    @Test
    public void loginTestFailure() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        serverFacade.logout(logoutRequest);
        LoginRequest loginRequest = new LoginRequest("will", "password");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login(loginRequest));
    }

    @Test
    public void logoutTestSucess() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(logoutRequest));
        //add a call to the server to show that you are not authenticated
    }

}
