package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import service.exception.AlreadyTakenException;
import service.exception.BadRequestException;
import service.exception.MessageWrapper;
import service.exception.UnauthorizedException;
import io.javalin.http.Context;
import requests.JoinGameRequest;
import responses.JoinGameResponse;
import service.JoinGameService;

public class JoinGameHandler {
    private MemoryDatabase memoryDatabase;
    private JoinGameService joinGameService;

    public JoinGameHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.joinGameService = new JoinGameService(memoryDatabase);
    }

    public void handle(Context ctx) {
        JoinGameRequest joinGameRequestBody = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        JoinGameRequest joinGameRequest = new JoinGameRequest(joinGameRequestBody.playerColor(), joinGameRequestBody.gameID(), ctx.header("authorization"));
        try {
            JoinGameResponse joinGameResponse = joinGameService.joinGame(joinGameRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(joinGameResponse));
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
