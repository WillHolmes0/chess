package dataaccess;

import model.AuthData;
import java.util.UUID;

public class AuthDAO {
    private MemoryDatabase memoryDatabase;

    public AuthDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public void addAuthToken(AuthData authData) {
        memoryDatabase.authTokens().put(authData.authToken(), authData);
    }

    public void deleteAuthToken(String authToken) {
        memoryDatabase.authTokens().remove(authToken);
    }

    public AuthData getAuthToken(String authToken) {
        return memoryDatabase.authTokens().get(authToken);
    }

    public void clearDatabase() {
        memoryDatabase.authTokens().clear();
    }
}
