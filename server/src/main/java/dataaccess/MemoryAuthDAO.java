package dataaccess;

import model.AuthData;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private MemoryDatabase memoryDatabase;

    public MemoryAuthDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    @Override
    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void addAuthToken(AuthData authData) {
        memoryDatabase.authTokens().put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuthToken(String authToken) {
        memoryDatabase.authTokens().remove(authToken);
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return memoryDatabase.authTokens().get(authToken);
    }

    @Override
    public void clearDatabase() {
        memoryDatabase.authTokens().clear();
    }
}
