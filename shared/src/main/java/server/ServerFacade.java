package server;

import com.google.gson.Gson;
import requests.*;
import responses.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResponse.class);
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        return handleResponse(response, LoginResponse.class);
    }

    public void clearDatabase() {
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        try {
            handleResponse(response, null);
            System.out.println("sucessfully cleared the database");
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        var request = buildRequest("DELETE", "/session", null, logoutRequest.authorization());
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) throws ResponseException {
        var request = buildRequest("GET", "/game", null, listGamesRequest.authorization());
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws ResponseException {
        CreateGameRequest createGameRequestBody = new CreateGameRequest(createGameRequest.gameName(), null);
        var request = buildRequest("POST", "/game", createGameRequestBody, createGameRequest.authorization());
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResponse.class);
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        JoinGameRequest joinGameRequestBody = new JoinGameRequest(joinGameRequest.playerColor(), joinGameRequest.gameID(), null);
        var request = buildRequest("PUT", "/game", joinGameRequestBody, joinGameRequest.authorization());
        var response = sendRequest(request);
        return handleResponse(response, JoinGameResponse.class);
    }

    public RemovePlayerResponse leaveGame(RemovePlayerRequest removePlayerRequest) throws ResponseException {
        RemovePlayerRequest removePlayerRequestBody = new RemovePlayerRequest(removePlayerRequest.gameID(), null);
        var request = buildRequest("DELETE", "/game", removePlayerRequestBody, removePlayerRequest.authorization());
        var response = sendRequest(request);
        return handleResponse(response, RemovePlayerResponse.class);
    }

    public UpdateGameResponse updateGame(UpdateGameRequest updateGameRequest) throws ResponseException {
        UpdateGameRequest updateGameRequestbody = new UpdateGameRequest(updateGameRequest.gameData(), null);
        var request = buildRequest("PUT", "/game/gamestate", updateGameRequestbody, updateGameRequest.authorization());
        var response = sendRequest(request);
        return handleResponse(response, UpdateGameResponse.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String auth) {
        HttpRequest.BodyPublisher requestBody = (body != null) ? HttpRequest.BodyPublishers.ofString(new Gson().toJson(body)) : HttpRequest.BodyPublishers.noBody();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, requestBody);
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (auth != null) {
            request.setHeader("authorization", auth);
        }
        return request.build();
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        return buildRequest(method, path, body, null);
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (status / 100 == 2) {
            if (responseClass != null) {
                return new Gson().fromJson(response.body(), responseClass);
            }
            return null;
        }
        ExceptionMessage message = new Gson().fromJson(response.body(), ExceptionMessage.class);
        throw new ResponseException(message.message());

    }
}

