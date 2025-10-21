package server.service;
import dataaccess.Gateway;
import dataaccess.MemoryDatabase;
import exception.alreadyTakenException;
import model.requests.RegisterRequest;
import model.responses.RegisterResponse;

public class RegisterService {
    private Gateway gateway;

    public RegisterService(MemoryDatabase memoryDatabase) {
        this.gateway = new Gateway(memoryDatabase);
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        String user = gateway.getUser(registerRequest.userData().username()).username();
        if (user == null) {
            gateway.addUser(registerRequest.userData());
            String authToken = gateway.generateAuthToken();
            gateway.addAuthToken(authToken);
            return new RegisterResponse(authToken);
        } else {
            throw new alreadyTakenException("username is already taken");
        }
    }
}
