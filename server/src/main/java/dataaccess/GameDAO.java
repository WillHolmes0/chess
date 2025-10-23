package dataaccess;

import chess.ChessGame;
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

    public void setPlayer(String username, ChessGame.TeamColor playerColor, int gameId) {
        GameData newGame;
        GameData game = memoryDatabase.games().get(String.valueOf(gameId));
        if (playerColor == ChessGame.TeamColor.WHITE) {
            newGame = new GameData(game.gameId(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            newGame = new GameData(game.gameId(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        memoryDatabase.games().put(String.valueOf(newGame.gameId()), newGame);
    }

    public void clearDatabase() {
        memoryDatabase.games().clear();
    }
}
