package server.service;

import dataaccess.AuthDAO;
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
        AuthDAO authDAO = new AuthDAO(memoryDatabase);
        AuthData authData = authDAO.getAuthToken(logoutRequest.authorization());
        if (authData != null) {
            authDAO.deleteAuthToken(logoutRequest.authorization());
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
