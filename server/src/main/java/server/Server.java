package server;

import dataaccess.MemoryDatabase;
import handlers.*;
import io.javalin.*;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Server {

    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private final Javalin javalin;
    private MemoryDatabase memoryDatabase;
    private WebSocketHandler webSocketHandler;

    public Server() {
        this.memoryDatabase = new MemoryDatabase();
        this.webSocketHandler = new WebSocketHandler();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/user", (Context ctx) -> new RegisterHandler(memoryDatabase).handle(ctx))
            .delete("/db", (Context ctx) -> new ClearApplicationHandler(memoryDatabase).handle(ctx))
            .post("/session", (Context ctx) -> new LoginHandler(memoryDatabase).handle(ctx))
            .delete("/session", (Context ctx) -> new LogoutHandler(memoryDatabase).handle(ctx))
            .post("/game", (Context ctx) -> new CreateGameHandler(memoryDatabase).handle(ctx))
            .get("/game", (Context ctx) -> new ListGamesHandler(memoryDatabase).handle(ctx))
            .put("/game", (Context ctx) -> new JoinGameHandler(memoryDatabase).handle(ctx))
            .delete("/game", (Context ctx) -> new RemovePlayerHandler(memoryDatabase).handle(ctx))
            .ws("/ws", ws -> {
                ws.onConnect(webSocketHandler);
                ws.onMessage(webSocketHandler);
                ws.onClose(webSocketHandler);
            });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
