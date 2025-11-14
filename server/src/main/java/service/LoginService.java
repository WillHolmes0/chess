package service;

import dataaccess.*;
import org.mindrot.jbcrypt.BCrypt;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;
import responses.LoginResponse;
import service.exception.UnauthorizedException;

public class LoginService {
    private MemoryDatabase memoryDatabase;

    public LoginService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) throws DataAccessException, AlreadyTakenException, BadRequestException, UnauthorizedException {
//        UserDAO userDAO = new MemoryUserDAO(memoryDatabase);
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
        UserDAO userDAO = new DatabaseUserDAO();
        AuthDAO authDAO = new DatabaseAuthDAO();

        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException("Error: missing field");
        }
        UserData userData = userDAO.getUser(loginRequest.username());
        if (userData != null) {
            if (userData.username().equals(loginRequest.username()) && BCrypt.checkpw(loginRequest.password(), userData.password())) {
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
