package service;

import dataaccess.*;
import service.exception.UnauthorizedException;
import model.AuthData;
import service.requests.LogoutRequest;
import service.responses.LogoutResponse;

public class LogoutService {
    private MemoryDatabase memoryDatabase;

    public LogoutService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public LogoutResponse logoutUser(LogoutRequest logoutRequest) throws UnauthorizedException, DataAccessException {
//        MemoryAuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
        AuthDAO authDAO = new DatabaseAuthDAO();
        AuthData authData = authDAO.getAuthData(logoutRequest.authorization());
        if (authData != null) {
            authDAO.deleteAuthToken(logoutRequest.authorization());
            return new LogoutResponse();
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
