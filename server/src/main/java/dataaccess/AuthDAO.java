package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public String generateAuthToken();

    public void addAuthToken(AuthData authData) throws DataAccessException;

    public void deleteAuthToken(String authToken) throws DataAccessException;

    public AuthData getAuthData(String authToken) throws DataAccessException;

    public void clearDatabase() throws DataAccessException;
}
