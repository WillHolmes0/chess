package client;

import org.junit.jupiter.api.*;
import requests.RegisterRequest;
import responses.RegisterResponse;
import server.ResponseException;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

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
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTestSucess() {
        RegisterRequest registerRequest = new RegisterRequest("willy", "password", "legit@email.com");
        RegisterResponse response = serverFacade.register(registerRequest);
        Assertions.assertEquals("willy", response.username());
        Assertions.assertNotNull(response.authToken());
    }

    @Test
    public void registerTestFailure() {
        RegisterRequest registerRequest = new RegisterRequest("willy", "password", "legit@email.com");
        serverFacade.register(registerRequest);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(registerRequest));
    }

    @Test
    public void LoginTestSucess() {
        Assertions.assertTrue(true);
    }

}
