package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import server.exception.AlreadyTakenException;
import server.exception.BadRequestException;
import server.exception.UnauthorizedException;
import io.javalin.http.Context;
import server.requests.JoinGameRequest;
import server.responses.JoinGameResponse;
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

        }
    }
}
