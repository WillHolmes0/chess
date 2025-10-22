package dataaccess;

import model.GameData;

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
}
