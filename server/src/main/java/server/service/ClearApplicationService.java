package server.service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryDatabase;
import dataaccess.UserDAO;
import model.GameData;

public class ClearApplicationService {
    private MemoryDatabase memoryDatabase;

    public ClearApplicationService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void clearAll() {
        AuthDAO authDAO = new AuthDAO(memoryDatabase);
        UserDAO userDAO = new UserDAO(memoryDatabase);
        GameDAO gameDAO = new GameDAO(memoryDatabase);
        authDAO.clearDatabase();
        userDAO.clearDatabase();
        gameDAO.clearDatabase();
    }
}