package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exception.UnauthorizedException;
import requests.ListGamesRequest;
import responses.ListGamesResponse;

public class ListGamesTests {

    private String authToken = "bypass";
    private MemoryDatabase memoryDatabase;

    @BeforeEach()
    public void setup() {
        memoryDatabase = new MemoryDatabase();
        memoryDatabase.authTokens().put("bypass", new AuthData("bypass", "user6"));

        memoryDatabase.games().put("1", new GameData(1, null, null, "game1", new ChessGame()));
        memoryDatabase.games().put("32", new GameData(32, "user1", null, "game2", new ChessGame()));
        memoryDatabase.games().put("1008", new GameData(1008, "user2", "user3", "game3", new ChessGame()));
        ClearApplicationService clearApplicationService = new ClearApplicationService(memoryDatabase);
        try {
            clearApplicationService.clearAll();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void listMultipleGamesSucessful() {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesService listGamesService = new ListGamesService(memoryDatabase);
        try {
            ListGamesResponse listGamesResponse = listGamesService.listGames(listGamesRequest);
            Assertions.assertEquals(3, listGamesResponse.games().size());
            Assertions.assertEquals(GameData.class, listGamesResponse.games().getFirst().getClass());
        } catch (Exception e) {}


    }


    @Test
    public void listGamesFailure() {
        ListGamesRequest listGamesRequest = new ListGamesRequest("invalidtoken");
        ListGamesService listGamesService = new ListGamesService(memoryDatabase);
        Assertions.assertThrows(UnauthorizedException.class, () -> listGamesService.listGames(listGamesRequest));
    }


}
