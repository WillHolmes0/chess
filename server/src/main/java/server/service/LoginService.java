package server.service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryDatabase;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import model.requests.LoginRequest;
import model.responses.LoginResponse;
import exception.UnauthorizedException;

public class LoginService {
    private MemoryDatabase memoryDatabase;

    public LoginService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        System.out.println(loginRequest.username());
        System.out.println(loginRequest.password());

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
                System.out.println("invalid");
                throw new UnauthorizedException("Error: unauthorized");
            }
        } else {
            System.out.println("was null");
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
