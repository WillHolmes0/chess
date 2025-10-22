package dataaccess;
import model.UserData;
import java.util.UUID;

public class UserDAO {
    private MemoryDatabase memoryDatabase;

    public UserDAO(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void addUser(UserData userData) {
        memoryDatabase.users().put(userData.username(), userData);
        System.out.println(memoryDatabase.users());
    }

    public UserData getUser(String username) {
        return memoryDatabase.users().get(username);
    }

    public void clearDatabase() {
        memoryDatabase.users().clear();
    }

}
