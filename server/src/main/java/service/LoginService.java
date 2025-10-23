package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryDatabase;
import dataaccess.MemoryUserDAO;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.responses.LoginResponse;
import service.exception.UnauthorizedException;

public class LoginService {
    private MemoryDatabase memoryDatabase;

    public LoginService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) throws DataAccessException, AlreadyTakenException, BadRequestException, UnauthorizedException {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException("Error: missing field");
        }
        MemoryUserDAO userDAO = new MemoryUserDAO(memoryDatabase);
        UserData userData = userDAO.getUser(loginRequest.username());
        if (userData != null) {
            if (userData.username().equals(loginRequest.username())  && userData.password().equals(loginRequest.password())) {
                MemoryAuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
                String authToken = authDAO.generateAuthToken();
                AuthData authData = new AuthData(authToken, userData.username());
                authDAO.addAuthToken(authData);
                return new LoginResponse(loginRequest.username(), authToken);
            } else {
                throw new UnauthorizedException("Error: invalid login credentials");
            }
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
