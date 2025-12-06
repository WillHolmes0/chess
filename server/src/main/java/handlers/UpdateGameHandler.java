package handlers;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import io.javalin.http.Context;
import requests.CreateGameRequest;
import requests.UpdateGameRequest;
import responses.CreateGameResponse;
import responses.UpdateGameResponse;
import service.CreateGameService;
import service.UpdateGameService;
import service.exception.BadRequestException;
import service.exception.MessageWrapper;
import service.exception.UnauthorizedException;

public class UpdateGameHandler {
    private MemoryDatabase memoryDatabase;
    private UpdateGameService updateGameService;

    public UpdateGameHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.updateGameService = new UpdateGameService(memoryDatabase);
    }

    public void handle(Context ctx) {
        UpdateGameRequest updateGameRequestBody = new Gson().fromJson(ctx.body(), UpdateGameRequest.class);
        UpdateGameRequest updateGameRequest = new UpdateGameRequest(updateGameRequestBody.chessMove(), updateGameRequestBody.gameID(), ctx.header("authorization"));
        try {
            UpdateGameResponse updateGameResponse = updateGameService.updateGame(updateGameRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(updateGameResponse));
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
