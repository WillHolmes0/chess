import jdk.javadoc.doclet.Reporter;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;
import server.ResponseException;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private boolean signedIn = false;
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

            try {
                result = eval(line);
                if (!result.equals("quit")) {
                    System.out.println(result);
                }
            } catch (Throwable e) {
                System.out.println(e);
            }
        }
    }

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().strip().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "signin" -> signIn(params);
                case "signout" -> signOut();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String signIn(String... params) {
        try {
            if (params.length == 2) {
            LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
            LoginResponse loginResponse = server.
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

    public String signOut() {
        if (signedIn) {
            signedIn = false;
            String message = String.format("%s signed out of the game", playerName);
            playerName = null;
            return message;
        }
        return null;
    }


    public String help() {
        if (signedIn) {
           return """
                    - createGame
                    - signOut
                    - quit
                   """;
        }
        return """
                - register <username> <password> <email>
                - signIn <username> <password>
                - quit
               """;
    }


}
