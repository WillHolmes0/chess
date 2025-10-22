package server.handlers;

import com.google.gson.Gson;
import dataaccess.MemoryDatabase;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.requests.CreateGameRequest;
import model.responses.CreateGameResponse;
import server.service.CreateGameService;
import io.javalin.http.Context;

public class CreateGameHandler {
    private MemoryDatabase memoryDatabase;
    private CreateGameService createGameService;

    public CreateGameHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.createGameService = new CreateGameService(memoryDatabase);
    }

    public void handle(Context ctx) {
        CreateGameRequest createGameRequestBody = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameRequest createGameRequest = new CreateGameRequest(createGameRequestBody.gameName(), ctx.header("authorization"));
        try {
            CreateGameResponse createGameResponse = createGameService.createGame(createGameRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(createGameResponse));
        } catch (BadRequestException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (UnauthorizedException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        }
    }

}
