package server.handlers;

import com.google.gson.Gson;
import dataaccess.MemoryDatabase;
import model.requests.LogoutRequest;
import server.service.LogoutService;
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
        logoutService.logoutUser(logoutRequest);
        ctx.status(200);
        ctx.result();
    }
}
