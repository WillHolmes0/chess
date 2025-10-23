package service;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryDatabase;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import model.AuthData;
import model.UserData;
import service.requests.RegisterRequest;
import service.responses.RegisterResponse;

public class RegisterService {
    private MemoryDatabase memoryDatabase;

    public RegisterService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException, BadRequestException {
        if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
            throw new BadRequestException("Error: missing field");
        }
        MemoryUserDAO userDAO = new MemoryUserDAO(memoryDatabase);
        UserData user = userDAO.getUser(registerRequest.username());
        if (user == null) {
            //Add the user to the User Database, then retrieve the username again for the request response which ensures retrieving the user works.
            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.addUser(userData);
            String retrievedUser = userDAO.getUser(registerRequest.username()).username();
            //Generate and Add authToken
            MemoryAuthDAO authDAO = new MemoryAuthDAO(memoryDatabase);
            String authToken = authDAO.generateAuthToken();
            AuthData authData = new AuthData(authToken, userData.username());
            authDAO.addAuthToken(authData);
            return new RegisterResponse(authToken, retrievedUser);
        } else {
            throw new AlreadyTakenException("Error: username is already taken");
        }
    }
}
