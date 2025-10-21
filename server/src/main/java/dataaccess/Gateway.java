package dataaccess;
import model.UserData;
import java.util.UUID;

public class Gateway {
    private MemoryDatabse memoryDatabase;

    public Gateway(MemoryDatabse memoryDatabase) {
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

    public void addAuthToken() {

    }
}
