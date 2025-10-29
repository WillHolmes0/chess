package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Set;

public interface GameDAO {

    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void setPlayer(String username, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException;

    void clearDatabase() throws DataAccessException;

    Set<String> getGameKeys() throws DataAccessException;
}
