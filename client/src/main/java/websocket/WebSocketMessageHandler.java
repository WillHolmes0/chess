package websocket;

import websocket.messages.ServerMessage;

public interface WebSocketMessageHandler {
    public void handleMessage(String message);
}
