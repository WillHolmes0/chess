package server.service;
import RequestModels.*;
import dataaccess.Gateway;
import dataaccess.MemoryDatabse;
import model.UserData;

public class RegisterService {
    private Gateway gateway;

    public RegisterService(MemoryDatabse memoryDatabase) {
        this.gateway = new Gateway(memoryDatabase);
    }

    public void registerUser(RegisterRequest registerRequest) {
        String user = gateway.getUser(registerRequest.userData().username()).username();
        if (user == null) {
            gateway.addUser(registerRequest.userData());
            String authId = gateway.generateAuthToken();

        } else {
            throw new alreadyTakenException();
        }

    }
}
