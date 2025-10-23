package handlers;

import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import io.javalin.http.Context;
import service.ClearApplicationService;

public class ClearApplicationHandler {
    private MemoryDatabase memoryDatabase;
    private ClearApplicationService clearApplicationService;

    public ClearApplicationHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.clearApplicationService = new ClearApplicationService(memoryDatabase);
    }

    public void handle(Context ctx) {
        try {
            clearApplicationService.clearAll();
            ctx.status(200);
            ctx.result();
        } catch (DataAccessException e) {

        }
    }
}
