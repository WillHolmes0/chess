package server.service;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.MemoryDatabase;
import exception.AlreadyTakenException;
import model.UserData;
import model.requests.RegisterRequest;
import model.responses.RegisterResponse;

public class RegisterService {
    private MemoryDatabase memoryDatabase;

    public RegisterService(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) throws AlreadyTakenException {
        UserDAO userDAO = new UserDAO(memoryDatabase);
        UserData user = userDAO.getUser(registerRequest.userData().username());
        if (user == null) {
            //Add the user to the User Database, then retrieve the username again for the request response which ensures retrieving the user works.
            userDAO.addUser(registerRequest.userData());
            String retrievedUser = userDAO.getUser(registerRequest.userData().username()).username();
            //Generate and Add authToken
            AuthDAO authDAO = new AuthDAO(memoryDatabase);
            String authToken = authDAO.generateAuthToken();
            authDAO.addAuthToken(authToken);
            return new RegisterResponse(authToken, retrievedUser);
        } else {
            throw new AlreadyTakenException("Error: username is already taken");
        }
    }
}
