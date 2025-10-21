package dataaccess;
import model.UserData;
import java.util.UUID;

public class Gateway {
    private MemoryDatabase memoryDatabase;

    public Gateway(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void addUser(UserData userData) {
        memoryDatabase.Users().put(userData.username(), userData);
        System.out.println(memoryDatabase.Users());
    }

    public UserData getUser(String username) {
        return memoryDatabase.Users().get(username);
    }

    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public void addAuthToken(String authToken) {
        memoryDatabase.authTokens().add(authToken);
    }
}
