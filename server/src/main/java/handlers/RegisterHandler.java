package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import io.javalin.http.Context;
import service.exception.MessageWrapper;
import requests.RegisterRequest;
import responses.RegisterResponse;
import service.RegisterService;

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
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(new Gson().toJson(new MessageWrapper(e.getMessage())));
        }
    }
}
