package server.service;

import dataaccess.AuthDAO;
import dataaccess.MemoryDatabase;
import dataaccess.UserDAO;

public class ClearApplicationService {
    private MemoryDatabase memoryDatabase;

    public ClearApplicationService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void clearAll() {
        AuthDAO authDAO = new AuthDAO(memoryDatabase);
        UserDAO userDAO = new UserDAO(memoryDatabase);
        authDAO.clearDatabase();
        userDAO.clearDatabase();
    }
}