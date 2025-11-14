package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import service.exception.MessageWrapper;
import service.exception.UnauthorizedException;
import requests.LogoutRequest;
import responses.LogoutResponse;
import service.LogoutService;
import io.javalin.http.Context;


public class LogoutHandler {
    private MemoryDatabase memoryDatabase;
    private LogoutService logoutService;

    public LogoutHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.logoutService = new LogoutService(memoryDatabase);
    }

    public void handle(Context ctx) {
        LogoutRequest logoutRequest = new LogoutRequest(ctx.header("authorization"));
        try {
            LogoutResponse logoutResponse = logoutService.logoutUser(logoutRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(logoutResponse));
        } catch (UnauthorizedException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(new Gson().toJson(new MessageWrapper(e.getMessage())));
        }
    }
}
