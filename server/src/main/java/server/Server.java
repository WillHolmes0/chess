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
            .post("/session", (Context ctx) -> new LoginHandler(memoryDatabase).handle(ctx));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
