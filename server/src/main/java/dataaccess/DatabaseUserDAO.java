package dataaccess;


import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO{

    public DatabaseUserDAO() throws DataAccessException {
            DatabaseManager.createDatabase();
            createTableIfNonexistant();
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        createTableIfNonexistant();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                """
                INSERT INTO users (username, password, email)
                VALUES (?, ?, ?)
                """;
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not add the user", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        createTableIfNonexistant();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                """
                SELECT * FROM users WHERE username=?
                """;
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        UserData result = new UserData(
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("email")
                        );
                        if (resultSet.next()) {
                            throw new DataAccessException("Error: more than one result for the provided username");
                        }
                        return result;
                    } else {
                        throw new DataAccessException("Error: specified user does not exist in the database");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not retrieve user", e);
        }
    }

    @Override
    public void clearDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            String statement = "DROP TABLE users";
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not drop 'users' table", e);
        }
    }

    private final String configureUserTableStatement =
            """
            CREATE TABLE IF NOT EXISTS users (
            username varchar(128) NOT NULL,
            password varchar(128) NOT NULL,
            email varchar(256) NOT NULL
            );
            """;

    private void createTableIfNonexistant() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(configureUserTableStatement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failure trying to create users table if it does not exist", e);
        }

    }
}


