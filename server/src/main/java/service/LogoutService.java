package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryDatabase;
import server.exception.UnauthorizedException;
import model.AuthData;
import server.requests.LogoutRequest;
import server.responses.LogoutResponse;

public class LogoutService {
    private MemoryDatabase memoryDatabase;

    public LogoutService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public LogoutResponse logoutUser(LogoutRequest logoutRequest) {
        MemoryAuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
        AuthData authData = authDAO.getAuthData(logoutRequest.authorization());
        if (authData != null) {
            authDAO.deleteAuthToken(logoutRequest.authorization());
            return new LogoutResponse();
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
