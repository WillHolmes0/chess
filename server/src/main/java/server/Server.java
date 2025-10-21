package server;

import dataaccess.MemoryDatabase;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import server.service.*;
import model.requests.RegisterRequest;
import model.UserData;
import model.responses.RegisterResponse;


public class Server {

    private final Javalin javalin;
    private RegisterService registerService;
    private MemoryDatabase memoryDatabase;

    public Server() {
        this.memoryDatabase = new MemoryDatabase();
        this.registerService = new RegisterService(memoryDatabase);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/user", this::register);

        // Register your endpoints and exception handlers here.

    }

    public void register(Context ctx) {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        RegisterRequest registerRequest = new RegisterRequest(userData);
        RegisterResponse registerResponse = registerService.registerUser(registerRequest);
        ctx.json(registerResponse.authToken());
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
