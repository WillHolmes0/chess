package websocket;

import jakarta.websocket.*;
import server.ResponseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade {
    Session session;

    public WebSocketFacade(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

        } catch (DeploymentException | IOException | URISyntaxException ex) {

        }
    }

    public void connect() {

    }
}
