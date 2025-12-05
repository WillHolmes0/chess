package websocket;

import com.google.gson.Gson;
import jakarta.websocket.*;
import server.ResponseException;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;

    public WebSocketFacade(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {

                    System.out.println(message);
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
}
