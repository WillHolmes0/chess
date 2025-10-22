package dataaccess;

import model.GameData;

import java.util.Set;

public class GameDAO {
    private MemoryDatabase memoryDatabase;

    public GameDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void createGame(GameData gameData) {
        memoryDatabase.games().put(String.valueOf(gameData.gameId()), gameData);
    }

    public GameData getGame(int gameId) {
        return memoryDatabase.games().get(String.valueOf(gameId));
    }

    public Set<String> getGameKeys() {
        return memoryDatabase.games().keySet();
    }

    public void clearDatabase() {
        memoryDatabase.games().clear();
    }
}
