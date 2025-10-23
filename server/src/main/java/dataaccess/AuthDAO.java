package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public String generateAuthToken();

    public void addAuthToken(AuthData authData);

    public void deleteAuthToken(String authToken);

    public AuthData getAuthData(String authToken);

    public void clearDatabase();
}
