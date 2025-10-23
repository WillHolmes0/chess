package server.service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryDatabase;
import exception.UnauthorizedException;
import model.AuthData;
import model.requests.LogoutRequest;

public class LogoutService {
    private MemoryDatabase memoryDatabase;

    public LogoutService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public void logoutUser(LogoutRequest logoutRequest) {
        MemoryAuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
        AuthData authData = authDAO.getAuthData(logoutRequest.authorization());
        if (authData != null) {
            authDAO.deleteAuthToken(logoutRequest.authorization());
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
