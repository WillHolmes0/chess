package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import service.exception.MessageWrapper;
import service.exception.UnauthorizedException;
import service.requests.ListGamesRequest;
import service.responses.ListGamesResponse;
import service.ListGamesService;
import io.javalin.http.Context;

public class ListGamesHandler {
    private MemoryDatabase memoryDatabase;
    private ListGamesService listGamesService;

    public ListGamesHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
        this.listGamesService = new ListGamesService(memoryDatabase);
    }

    public void handle(Context ctx) {
        try {
            ListGamesRequest listGamesRequest = new ListGamesRequest(ctx.header("authorization"));
            ListGamesResponse listGamesResponse = listGamesService.listGames(listGamesRequest);
            System.out.println(listGamesResponse);
            ctx.status(200);
            ctx.result(new Gson().toJson(listGamesResponse));
        } catch (UnauthorizedException e) {
            ctx.status(e.getCode());
            ctx.result(new Gson().toJson(e.messageWrapper()));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(new Gson().toJson(new MessageWrapper(e.getMessage())));
        }
    }
}
