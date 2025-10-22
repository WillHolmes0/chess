package server.handlers;

import com.google.gson.Gson;
import dataaccess.MemoryDatabase;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import io.javalin.http.Context;
import model.UserData;
import model.requests.RegisterRequest;
import model.responses.RegisterResponse;
import server.service.ClearApplicationService;
import exception.MessageWrapper;

public class ClearApplicationHandler {
    private MemoryDatabase memoryDatabase;
    private ClearApplicationService clearApplicationService;

    public ClearApplicationHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.clearApplicationService = new ClearApplicationService(memoryDatabase);
    }

    public void handle(Context ctx) {
        clearApplicationService.clearAll();
        ctx.status(200);
        ctx.result();
    }
}
