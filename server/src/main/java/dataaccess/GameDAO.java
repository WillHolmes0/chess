package dataaccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {

    void createGame(GameData gameData);

    GameData getGame(int gameID);

    void setPlayer(String username, ChessGame.TeamColor playerColor, int gameID);

    void clearDatabase();
}
