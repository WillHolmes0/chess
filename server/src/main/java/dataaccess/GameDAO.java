package dataaccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {

    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void setPlayer(String username, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException;

    void clearDatabase() throws DataAccessException;
}
