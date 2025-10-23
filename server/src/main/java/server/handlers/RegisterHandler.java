package server.handlers;

import com.google.gson.Gson;
import dataaccess.MemoryDatabase;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import io.javalin.http.Context;
import model.UserData;
import model.requests.RegisterRequest;
import model.responses.RegisterResponse;
import server.service.RegisterService;

public class RegisterHandler {
    private RegisterService registerService;
    private MemoryDatabase memoryDatabase;

    public RegisterHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.registerService = new RegisterService(memoryDatabase);
    }

    public void handle(Context ctx) {
        try {
            RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            RegisterResponse registerResponse = registerService.registerUser(registerRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(registerResponse));
        } catch (BadRequestException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (AlreadyTakenException e) {
            System.out.println(e.messageWrapper());
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        }
    }
}
