package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DatabaseGameDAO implements GameDAO {

    public DatabaseGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        createTableIfNonexistant();
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        createTableIfNonexistant();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                """
                INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game)
                VALUES(?, ?, ?, ?, ?);
                """;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                String serializedGame = new Gson().toJson(gameData.game());
                ps.setInt(1, gameData.gameID());
                ps.setString(2, gameData.whiteUsername());
                ps.setString(3, gameData.blackUsername());
                ps.setString(4, gameData.gameName());
                ps.setString(5, serializedGame);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not create chessgame", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                """
                SELECT * FROM games
                WHERE gameID=?;
                """;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        GameData result;
                        result = new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("game"), ChessGame.class)
                        );
                        if (rs.next()) {
                            throw new DataAccessException("Error: got more than one result for the given gameId");
                        }
                        return result;
                    } else {
                        throw new DataAccessException("Error: got no results for the given gameId");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: cannot get game", e);
        }
    }

    @Override
    public void setPlayer(String username, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement;
            if (playerColor == ChessGame.TeamColor.WHITE) {
                statement = """
                        UPDATE games
                        SET whiteUsername=?
                        WHERE gameID=?;
                        """;
            } else {
                statement = """
                        UPDATE games
                        SET blackUsername=?
                        WHERE gameID=?;
                        """;
            }
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not add specified player to specified color", e);
        }
    }

    @Override
    public Set<String> getGameKeys() throws DataAccessException {
        Set<String> gameKeys = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        gameKeys.add(rs.getString("gameID"));
                        while (rs.next()) {
                            gameKeys.add(rs.getString("gameID"));
                        }
                        return gameKeys;
                    } else {
                        throw new DataAccessException("Error: returned no keys");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not retrieve game keys", e);
        }
    }

    @Override
    public void clearDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                    """
                    DROP TABLE games;
                    """;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not drop 'games' table", e);
        }
    }


    private void createTableIfNonexistant() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = """
                    CREATE TABLE IF NOT EXISTS games (
                    gameID int NOT NULL,
                    whiteUsername varchar(128),
                    blackUsername varchar(128),
                    gameName varchar(256) NOT NULL,
                    game varchar(2048) NOT NULL
                    );
                    """;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not create 'games' table");
        }
    }
}
