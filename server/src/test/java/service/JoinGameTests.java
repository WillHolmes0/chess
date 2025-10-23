package service;

import chess.ChessGame;
import dataaccess.MemoryDatabase;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import service.exception.AlreadyTakenException;
import service.requests.JoinGameRequest;

public class JoinGameTests {
    private String authToken = "bypass";
    private MemoryDatabase memoryDatabase;

    @BeforeEach()
    public void setup() {
        memoryDatabase = new MemoryDatabase();
        memoryDatabase.authTokens().put("bypass", new AuthData("bypass", "user6"));
    }


    @Test
    public void joinGameSucess() {
        memoryDatabase.games().put("1", new GameData(1, null, null, "game1", new ChessGame()));

        JoinGameService joinGameService = new JoinGameService(memoryDatabase);
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1, "bypass");
        try {
            joinGameService.joinGame(joinGameRequest);
        } catch (Exception e) {}

        GameData game = memoryDatabase.games().get("1");
        Assertions.assertEquals(1, memoryDatabase.games().size());
        Assertions.assertEquals("user6", game.whiteUsername());
        Assertions.assertNull(game.blackUsername());

    }


    @Test
    public void colorTakenException() {
        memoryDatabase.games().put("1", new GameData(1, "user7", null, "game1", new ChessGame()));

        JoinGameService joinGameService = new JoinGameService(memoryDatabase);
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1, "bypass");

        Assertions.assertThrows(AlreadyTakenException.class, () -> joinGameService.joinGame(joinGameRequest));
    }

}
