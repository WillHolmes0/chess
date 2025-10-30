package dataaccess;

import model.AuthData;
import org.xml.sax.SAXException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO {

    public DatabaseAuthDAO() throws DataAccessException{
        DatabaseManager.createDatabase();
        createTableIfNotExists();
    }

    @Override
    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                """
                SELECT * FROM authTokens WHERE authToken=?;
                """;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        AuthData result = new AuthData(
                            rs.getString("authToken"),
                            rs.getString("username")
                        );
                        if (rs.next()) {
                            throw new DataAccessException("Error: more than one result for the provided AuthToken");
                        }
                        return result;
                    } else {
                        throw new DataAccessException("Error: no results for the provided AuthToken");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not get authData from provided authToken", e);
        }
    }

    @Override
    public void addAuthToken(AuthData authData) throws DataAccessException {
        createTableIfNotExists();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                """
                INSERT INTO authTokens (authToken, username)
                VALUES (?, ?)
                """;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                ps.setString(2, authData.username());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not add authToken to authTokens table", e);
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                    """
                    DELETE FROM authTokens
                    WHERE authToken=?;
                    """;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not delete the AuthToken row specified", e);
        }
    }

    @Override
    public void clearDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "DROP TABLE authTokens;";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not clear 'authTokens' table");
        }
    }

    private void createTableIfNotExists() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                """
                CREATE TABLE IF NOT EXISTS authTokens (
                authToken varchar(256) NOT NULL,
                username varchar(128) NOT NULL
                );
                """;
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failure trying to create authTokens table if it does not exist", e);
        }
    }
}