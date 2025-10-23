package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import server.exception.BadRequestException;
import server.exception.UnauthorizedException;
import server.requests.CreateGameRequest;
import server.responses.CreateGameResponse;
import service.CreateGameService;
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
        } catch (DataAccessException e) {

        }
    }

}
