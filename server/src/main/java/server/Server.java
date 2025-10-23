package server;

import dataaccess.MemoryDatabase;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import server.handlers.*;
import server.service.*;
import model.requests.RegisterRequest;
import model.UserData;
import model.responses.RegisterResponse;
import exception.*;


public class Server {

    private final Javalin javalin;
    private MemoryDatabase memoryDatabase;

    public Server() {
        this.memoryDatabase = new MemoryDatabase();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/user", (Context ctx) -> new RegisterHandler(memoryDatabase).handle(ctx))
            .delete("/db", (Context ctx) -> new ClearApplicationHandler(memoryDatabase).handle(ctx))
            .post("/session", (Context ctx) -> new LoginHandler(memoryDatabase).handle(ctx))
            .delete("/session", (Context ctx) -> new LogoutHandler(memoryDatabase).handle(ctx))
            .post("/game", (Context ctx) -> new CreateGameHandler(memoryDatabase).handle(ctx))
            .get("/game", (Context ctx) -> new ListGamesHandler(memoryDatabase).handle(ctx))
            .put("/game", (Context ctx) -> new JoinGameHandler(memoryDatabase).handle(ctx));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
