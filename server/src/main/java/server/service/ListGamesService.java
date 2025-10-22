package server.service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryDatabase;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.requests.ListGamesRequest;
import model.responses.ListGamesResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ListGamesService {
    private MemoryDatabase memoryDatabase;

    public ListGamesService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) {
        AuthDAO authDAO = new AuthDAO(memoryDatabase);
        AuthData authData = authDAO.getAuthData(listGamesRequest.authorization());
        if (authData != null) {
            GameDAO gameDAO = new GameDAO(memoryDatabase);
            Set<String> gameKeySet = gameDAO.getGameKeys();
            ArrayList<GameData> gameList = new ArrayList<>();
            for (String key : gameKeySet) {
                gameList.add(gameDAO.getGame(Integer.valueOf(key)));
            }
            return new ListGamesResponse(gameList);
//            return new ListGamesResponse(memoryDatabase.games());
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
