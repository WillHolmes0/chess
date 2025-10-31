package service;

import dataaccess.*;
import service.exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import service.requests.ListGamesRequest;
import service.responses.ListGamesResponse;

import java.util.ArrayList;
import java.util.Set;

public class ListGamesService {
    private MemoryDatabase memoryDatabase;

    public ListGamesService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) throws DataAccessException, UnauthorizedException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        GameDAO gameDAO = new DatabaseGameDAO();

        AuthData authData = authDAO.getAuthData(listGamesRequest.authorization());
        if (authData != null) {
            Set<String> gameKeySet = gameDAO.getGameKeys();
            ArrayList<GameData> gameList = new ArrayList<>();
            for (String key : gameKeySet) {
                gameList.add(gameDAO.getGame(Integer.valueOf(key)));
            }
            return new ListGamesResponse(gameList);
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
