package dataaccess;


import model.UserData;

import java.sql.Connection;
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
        try (var conn = DatabaseManager.getConnection()) {
            String statement =
                """
                INSERT INTO users (id, name)
                VALUES (1, 'testuser');
                """;
            try (var preparedStatement = conn.prepareStatement(statement)) {
                var result = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not add the user", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
//        try (Connection conn = DatabaseManager.getConnection()) {
//            var statement
//        }
        throw new DataAccessException("not implemented");
    }

    @Override
    public void clearDatabase() throws DataAccessException {

    }

    private final String configureUserTableStatement =
            """
            CREATE TABLE IF NOT EXISTS users (
            id int,
            name varchar(128)
            )
            """;

    private void configureUserDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(configureUserTableStatement)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to configure the user table", e);
        }

    }
}


