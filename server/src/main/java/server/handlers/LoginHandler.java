package server.handlers;

import com.google.gson.Gson;
import dataaccess.MemoryDatabase;
import exception.BadRequestException;
import exception.UnauthorizedException;
import io.javalin.http.Context;
import model.requests.LoginRequest;
import server.service.LoginService;
import model.responses.LoginResponse;

public class LoginHandler {
    private MemoryDatabase memoryDatabase;
    private LoginService loginService;

    public LoginHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.loginService = new LoginService(memoryDatabase);
    }

    public void handle(Context ctx) {
        try {
            LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
            if (loginRequest.username() == null || loginRequest.password() == null) {
                throw new BadRequestException("Error: bad request");
            }
            LoginResponse loginResponse = loginService.loginUser(loginRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(loginResponse));
        } catch (BadRequestException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (UnauthorizedException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        }
    }
}
