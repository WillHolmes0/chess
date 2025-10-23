package service;

import dataaccess.*;

public class ClearApplicationService {
    private MemoryDatabase memoryDatabase;

    public ClearApplicationService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void clearAll() throws DataAccessException {
        MemoryAuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
        MemoryUserDAO userDAO = new MemoryUserDAO(memoryDatabase);
        MemoryGameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        authDAO.clearDatabase();
        userDAO.clearDatabase();
        gameDAO.clearDatabase();
    }
}