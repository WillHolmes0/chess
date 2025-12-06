package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import jakarta.websocket.*;
import server.ResponseException;
import server.ServerFacade;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;

    public WebSocketFacade(String url, WebSocketMessageHandler webSocketMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    webSocketMessageHandler.handleMessage(message);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void leave(int gameID, String authentication) {
        UserGameCommand userGameCommand = new UserGameCommand((UserGameCommand.CommandType.LEAVE), authentication, gameID);
        session.getAsyncRemote().sendText(new Gson().toJson(userGameCommand));
    }

    public void makeMove(ChessMove chessMove, int gameID, String authentication) {
        MakeMoveCommand makeMoveCommand = new MakeMoveCommand(chessMove, gameID, authentication);
        session.getAsyncRemote().sendText(new Gson().toJson(makeMoveCommand));
    }


}
