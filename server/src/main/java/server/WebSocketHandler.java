package server;

import com.google.gson.Gson;
import dataaccess.MemoryDatabase;
import handlers.RemovePlayerHandler;
import io.javalin.websocket.*;
import requests.RemovePlayerRequest;
import responses.RemovePlayerResponse;
import service.RemovePlayerService;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import javax.management.Notification;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    MemoryDatabase memoryDatabase = new MemoryDatabase();


    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Connected to websocket");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            RemovePlayerService removePlayerService = new RemovePlayerService(memoryDatabase);
            RemovePlayerRequest removePlayerRequest = new RemovePlayerRequest(userGameCommand.getGameID(), userGameCommand.getAuthToken());
            RemovePlayerResponse removePlayerResponse = removePlayerService.removePlayer(removePlayerRequest);
            NotificationMessage notificationMessage = new NotificationMessage(String.format("%s, player %s left the game", removePlayerResponse.color(), removePlayerResponse.username()));
            ctx.session.getRemote().sendString(new Gson().toJson(notificationMessage));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("websocket closed");
    }
}
