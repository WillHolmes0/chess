package server;

import dataaccess.MemoryDatabse;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import server.service.*;
import RequestModels.RegisterRequest;
import server.service.*;
import model.UserData;


public class Server {

    private final Javalin javalin;
    private RegisterService registerService;
    private MemoryDatabse memoryDatabse;

    public Server() {
        this.memoryDatabse = new MemoryDatabse();
        this.registerService = new RegisterService(memoryDatabse);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/user", this::register);

        // Register your endpoints and exception handlers here.

    }

    public void register(Context ctx) {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        RegisterRequest registerRequest = new RegisterRequest(userData);
        registerService.registerUser(registerRequest);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
