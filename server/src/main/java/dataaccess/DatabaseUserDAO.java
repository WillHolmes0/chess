package dataaccess;


import model.UserData;

import java.sql.SQLException;

public class DatabaseUserDAO implements UserDAO{

    public DatabaseUserDAO() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {

        }
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("")) {
                var result = preparedStatement.executeQuery();
            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not add the user", e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        throw new DataAccessException("not implmented");
    }

    @Override
    public void clearDatabase() throws DataAccessException {

    }
}


