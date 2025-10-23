package dataaccess;
import model.UserData;

public class MemoryUserDAO implements UserDAO {
    private MemoryDatabase memoryDatabase;

    public MemoryUserDAO(MemoryDatabase memoryDatabase) {
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
