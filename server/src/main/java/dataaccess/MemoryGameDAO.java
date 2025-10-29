package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Set;

public class MemoryGameDAO implements GameDAO {
    private MemoryDatabase memoryDatabase;

    public MemoryGameDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    @Override
    public void createGame(GameData gameData) {
        memoryDatabase.games().put(String.valueOf(gameData.gameID()), gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        return memoryDatabase.games().get(String.valueOf(gameID));
    }

    @Override
    public Set<String> getGameKeys() throws DataAccessException {
        return memoryDatabase.games().keySet();
    }

    @Override
    public void setPlayer(String username, ChessGame.TeamColor playerColor, int gameID) {
        GameData newGame;
        GameData game = memoryDatabase.games().get(String.valueOf(gameID));
        if (playerColor == ChessGame.TeamColor.WHITE) {
            newGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            newGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        memoryDatabase.games().put(String.valueOf(newGame.gameID()), newGame);
    }

    @Override
    public void clearDatabase() {
        memoryDatabase.games().clear();
    }
}
