package client;

import model.GameData;
import org.junit.jupiter.api.*;
import requests.*;
import responses.ListGamesResponse;
import responses.LoginResponse;
import responses.RegisterResponse;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.util.List;


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
        CreateGameRequest createGameRequest = new CreateGameRequest("newGame", authToken);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame(createGameRequest));
    }

    @Test
    public void createGameTestSuccess() {
        CreateGameRequest createGameRequest = new CreateGameRequest("myGame", authToken);
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(createGameRequest));
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResponse listGamesResponse = serverFacade.listGames(listGamesRequest);
        Assertions.assertEquals(1, listGamesResponse.games().size());
        Assertions.assertInstanceOf(GameData.class, listGamesResponse.games().get(0));
    }

    @Test
    public void createGameTestFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("myGame", "invalid AuthToken");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame(createGameRequest));
    }

    @Test
    public void listGamesSuccess() {
        CreateGameRequest createGameRequest = new CreateGameRequest("myGame", authToken);
        serverFacade.createGame(createGameRequest);
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResponse listGamesResponse = serverFacade.listGames(listGamesRequest);
        model.GameData game = listGamesResponse.games().get(0);
        Assertions.assertEquals(1, listGamesResponse.games().size());
        Assertions.assertInstanceOf(GameData.class, game);
        Assertions.assertEquals("myGame", game.gameName());
    }

    @Test
    public void listGamesFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("myGame", authToken);
        serverFacade.createGame(createGameRequest);
        ListGamesRequest listGamesRequest = new ListGamesRequest("invalid auth token");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames(listGamesRequest));
    }



}
