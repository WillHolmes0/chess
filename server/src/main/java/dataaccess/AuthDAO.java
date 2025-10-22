package dataaccess;

import java.util.UUID;

public class AuthDAO {
    private MemoryDatabase memoryDatabase;

    public AuthDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public void addAuthToken(String authToken) {
        memoryDatabase.authTokens().add(authToken);
    }

    public void clearDatabase() {
        memoryDatabase.authTokens().clear();
    }
}
