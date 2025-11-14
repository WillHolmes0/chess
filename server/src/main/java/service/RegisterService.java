package service;
import dataaccess.*;
import org.mindrot.jbcrypt.BCrypt;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import model.AuthData;
import model.UserData;
import requests.RegisterRequest;
import responses.RegisterResponse;

public class RegisterService {
    private MemoryDatabase memoryDatabase;

    public RegisterService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException, BadRequestException {
//        UserDAO userDAO = new MemoryUserDAO(memoryDatabase);
//        AuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
        UserDAO userDAO = new DatabaseUserDAO();
        AuthDAO authDAO = new DatabaseAuthDAO();
        if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
            throw new BadRequestException("Error: missing field");
        }
        UserData user = userDAO.getUser(registerRequest.username());
        if (user == null) {
            //Add the user to the User Database, then retrieve the username again for the request response which ensures retrieving the user works.
            String encryptedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
            UserData userData = new UserData(registerRequest.username(), encryptedPassword, registerRequest.email());
            userDAO.addUser(userData);
            String retrievedUser = userDAO.getUser(registerRequest.username()).username();
            //Generate and Add authToken
            String authToken = authDAO.generateAuthToken();
            AuthData authData = new AuthData(authToken, userData.username());
            authDAO.addAuthToken(authData);
            System.out.println(retrievedUser);
            return new RegisterResponse(authToken, retrievedUser);
        } else {
            throw new AlreadyTakenException("Error: username is already taken");
        }
    }
}
