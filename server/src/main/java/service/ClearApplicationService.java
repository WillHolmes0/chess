package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryDatabase;
import dataaccess.MemoryUserDAO;

public class ClearApplicationService {
    private MemoryDatabase memoryDatabase;

    public ClearApplicationService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void clearAll() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
        MemoryUserDAO userDAO = new MemoryUserDAO(memoryDatabase);
        MemoryGameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        authDAO.clearDatabase();
        userDAO.clearDatabase();
        gameDAO.clearDatabase();
    }
}