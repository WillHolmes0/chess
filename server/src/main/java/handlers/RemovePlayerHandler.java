package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import requests.RemovePlayerRequest;
import responses.RemovePlayerResponse;
import service.RemovePlayerService;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import service.exception.MessageWrapper;
import service.exception.UnauthorizedException;
import io.javalin.http.Context;
import requests.JoinGameRequest;
import responses.JoinGameResponse;
import service.JoinGameService;

public class RemovePlayerHandler {
    private MemoryDatabase memoryDatabase;
    private RemovePlayerService removePlayerService;

    public RemovePlayerHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.removePlayerService = new RemovePlayerService(memoryDatabase);
    }

    public void handle(Context ctx) {
        RemovePlayerRequest removePlayerRequestBody = new Gson().fromJson(ctx.body(), RemovePlayerRequest.class);
        RemovePlayerRequest removePlayerRequest = new RemovePlayerRequest(removePlayerRequestBody.gameID(), ctx.header("authorization"));
        try {
            RemovePlayerResponse removePlayerResponse = removePlayerService.removePlayer(removePlayerRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(removePlayerResponse));
        } catch (UnauthorizedException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (AlreadyTakenException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (BadRequestException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(new Gson().toJson(new MessageWrapper(e.getMessage())));
        }
    }
}