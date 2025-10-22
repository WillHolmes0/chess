package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;

import model.AuthData;
import model.UserData;

public class MemoryDatabase {
    private HashMap<String, UserData> users;
    private HashMap<String, AuthData> authTokens;

    public MemoryDatabase() {
        users = new HashMap<>();
        authTokens = new HashMap<>();
    }

    public HashMap<String, UserData> Users() {
        return users;
    }

    public HashMap<String, AuthData> authTokens() { return authTokens;}
}
