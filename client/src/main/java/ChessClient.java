import model.GameData;
import requests.*;
import responses.*;
import server.ResponseException;
import server.ServerFacade;

import javax.swing.*;
import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private String authToken = null;
    private String playerName;
    private ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to the ChessServer");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            result = eval(line);
            if (!result.equals("quit")) {
                System.out.println(result);
            }
        }
    }


    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().strip().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (authToken == null) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            }
            return switch (cmd) {
                case "joingame" -> joinGame(params);
                case "listgames" -> listGames();
                case "creategame" -> createGame(params);
                case "logout" -> logOut();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }


    public String login(String... params) {
        try {
            if (params.length == 2) {
                LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
                LoginResponse loginResponse = server.login(loginRequest);
                authToken = loginResponse.authToken();
                return String.format("logged in as %s", loginResponse.username());
            }
            throw new ResponseException("Error: Invalid amount of arguments");
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
            if (params.length == 3) {
                RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
                RegisterResponse registerResponse = server.register(registerRequest);
                authToken = registerResponse.authToken();
                return String.format("registered as %s", registerResponse.username());
            }
            throw new ResponseException("Error: incorrect number of inputs supplied");
    }

    public String logOut() throws ResponseException {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        server.logout(logoutRequest);
        authToken = null;
        return String.format("successfully logged out");
    }

    public String createGame(String... params) {
        if (params.length == 1) {
            CreateGameRequest createGameRequest = new CreateGameRequest(params[0], authToken);
            CreateGameResponse createGameResponse = server.createGame(createGameRequest);
            return String.format("created game: %s. Game ID is %d", createGameRequest.gameName(), createGameResponse.gameID());
        } else {
            throw new ResponseException("Error: incorrect number of inputs supplied");
        }
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            String color = params[0].toLowerCase().strip();
            int gameID = Integer.parseInt(params[1]);
            JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID, authToken);
            JoinGameResponse joinGameResponse = server.joinGame(joinGameRequest);
            return String.format("Joined game no. %s, as %s player", gameID, color);
        }
        throw new ResponseException("Error: incorrect number of inputs supplied");
    }

    public String listGames() throws ResponseException {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResponse listGamesResponse = server.listGames(listGamesRequest);
        String gameList = "\n";
        int counter = 1;
        if (listGamesResponse.games().size() == 0) {
            return "\nNo games to list\n";
        }
        for (GameData game : listGamesResponse.games()) {
            String gameInfo = String.format("%d. Game Name: %s  Game ID; %s\n", counter, game.game(), game.gameID());
            gameList = gameList + gameInfo;
        }
        return gameList;
    }

    public String help() {
        if (authToken != null) {
           return """
                    - creategame <gameName>
                    - joingame <playerColor>(white or black) <gameID>
                    - listgames
                    - observegame <gameID>
                    - logOut
                    - quit
                   """;
        }
        return """
                - register <username> <password> <email>
                - logIn <username> <password>
                - quit
               """;
    }


}
