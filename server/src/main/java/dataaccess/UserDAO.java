package dataaccess;
import model.UserData;
import java.util.UUID;

public class UserDAO {
    private MemoryDatabase memoryDatabase;

    public UserDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void addUser(UserData userData) {
        memoryDatabase.Users().put(userData.username(), userData);
        System.out.println(memoryDatabase.Users());
    }

    public UserData getUser(String username) {
        return memoryDatabase.Users().get(username);
    }

    public void clearDatabase() {
        memoryDatabase.Users().clear();
    }

}
