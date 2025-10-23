package dataaccess;

import model.UserData;

public interface UserDAO {

    void addUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearDatabase() throws DataAccessException;
}
