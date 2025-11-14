package server;

import com.google.gson.Gson;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;

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

    public RegisterResponse register(RegisterRequest registerRequest) {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResponse.class);
    }

    public LoginResponse login(LoginResponse loginRequest) {
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        return handleResponse(response, LoginResponse.class);
    }

    public void logout(LogoutRequest logoutRequest) {
        var request = buildRequest("DELETE", "/session", logoutRequest);
        sendRequest(request);
    }


    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return null;
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (isSuccessful(status)) {
            if (responseClass != null) {
                return new Gson().fromJson(response.body(), responseClass);
            }
            return null;
        }
        throw new ResponseException(response.body());
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
