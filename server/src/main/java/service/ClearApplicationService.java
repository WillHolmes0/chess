package service;

import dataaccess.*;
import org.eclipse.jetty.server.Authentication;

public class ClearApplicationService {
    private MemoryDatabase memoryDatabase;

    public ClearApplicationService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void clearAll() throws DataAccessException {
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
//        UserDAO userDAO = new MemoryUserDAO(memoryDatabase);
//        GameDAO gameDAO = new MemoryGameDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        UserDAO userDAO = new DatabaseUserDAO();
        GameDAO gameDAO = new DatabaseGameDAO();
        authDAO.clearDatabase();
        userDAO.clearDatabase();
        gameDAO.clearDatabase();
    }
}