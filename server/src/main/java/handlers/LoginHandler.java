package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import service.exception.BadRequestException;
import service.exception.MessageWrapper;
import service.exception.UnauthorizedException;
import io.javalin.http.Context;
import service.requests.LoginRequest;
import service.LoginService;
import service.responses.LoginResponse;

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
            LoginResponse loginResponse = loginService.loginUser(loginRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(loginResponse));
        } catch (BadRequestException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (UnauthorizedException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(new Gson().toJson(new MessageWrapper(e.getMessage())));
        }
    }
}
