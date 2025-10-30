package dataaccess;


import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO{

    public DatabaseUserDAO() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            configureUserDatabase();
        } catch (DataAccessException e) {

        }
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement =
                """
                INSERT INTO users (username, password, email)
                VALUES (?, ?, ?)
                """;
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                System.out.println("part");
                preparedStatement.setString(1, userData.username());
                System.out.println("part");
                preparedStatement.setString(2, userData.password());
                System.out.println("part");
                preparedStatement.setString(3, userData.email());
                System.out.println("part");
                preparedStatement.executeUpdate();
                System.out.println("part");
            } catch (SQLException e) {
                throw new DataAccessException("inner block", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not add the user", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
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

    }

    private final String configureUserTableStatement =
            """
            CREATE TABLE IF NOT EXISTS users (
            username varchar(128),
            password varchar(128),
            email varchar(256)
            )
            """;

    private void configureUserDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(configureUserTableStatement)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to configure the user table", e);
        }

    }
}


