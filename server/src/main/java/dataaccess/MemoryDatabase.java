package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;

import model.AuthData;
import model.GameData;
import model.UserData;

public class MemoryDatabase {
    private HashMap<String, UserData> users;
    private HashMap<String, AuthData> authTokens;
    private HashMap<String, GameData> games;

    public MemoryDatabase() {
        users = new HashMap<>();
        authTokens = new HashMap<>();
        games = new HashMap<>();
    }

    public HashMap<String, UserData> users() {
        return users;
    }

    public HashMap<String, AuthData> authTokens() { return authTokens;}

    public HashMap<String, GameData> games() { return games; }
}
