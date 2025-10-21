package dataaccess;

import java.util.HashMap;
import java.util.Map;

import model.UserData;

public class MemoryDatabse {
    private Map<String, UserData> users;
    private Map<String, String> authTokens;

    public MemoryDatabse() {
        users = new HashMap<>();
        authTokens = new HashMap<>();
    }

    public Map<String, UserData> Users() {
        return users;
    }

    public Map<String, String> authTokens() { return authTokens;}
}
