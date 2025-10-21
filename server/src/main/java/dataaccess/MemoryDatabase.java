package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;

import model.UserData;

public class MemoryDatabase {
    private HashMap<String, UserData> users;
    private ArrayList<String> authTokens;

    public MemoryDatabase() {
        users = new HashMap<>();
        authTokens = new ArrayList<>();
    }

    public HashMap<String, UserData> Users() {
        return users;
    }

    public ArrayList<String> authTokens() { return authTokens;}
}
