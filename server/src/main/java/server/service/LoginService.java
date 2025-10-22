package server.service;

import dataaccess.AuthDAO;
import dataaccess.MemoryDatabase;
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

        UserData userData = memoryDatabase.Users().get(loginRequest.username());
        if (userData != null) {
            if (userData.username().equals(loginRequest.username())  && userData.password().equals(loginRequest.password())) {
                AuthDAO authDAO = new AuthDAO(memoryDatabase);
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
