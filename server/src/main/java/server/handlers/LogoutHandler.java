package server.handlers;

import com.google.gson.Gson;
import dataaccess.MemoryDatabase;
import exception.UnauthorizedException;
import model.requests.LogoutRequest;
import model.responses.LogoutResponse;
import server.service.LogoutService;
import io.javalin.http.Context;
import exception.MessageWrapper;


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
        }
    }
}
