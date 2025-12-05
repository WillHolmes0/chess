import chess.ChessGame;
import model.GameData;
import requests.*;
import responses.*;
import server.ResponseException;
import server.ServerFacade;

import java.util.*;

public class ChessClient extends UiBase {
    private String authToken = null;
    private String playerName;
    private ServerFacade server;
    private Map<String, GameData> gameList = new HashMap<>();
    private String serverUrl;
    private boolean enterGameUi = false;
    private String currentChessGameNo;
    private ChessGame.TeamColor currentChessPerspective;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to the ChessServer");

        //chessboard display for testing
        System.out.println(login("r", "t"));
        System.out.println(listGames());
//        System.out.println(leaveGame("1"));
        System.out.println(joinGame("black", "1"));
//        String observableGame = observeGame("9764");
//        System.out.print(observableGame);
        //end testing code

        Scanner scanner = new Scanner(System.in);
        var input = "";
        while (!input.equals("quit")) {
            System.out.print("\n>>> ");
            input = scanner.nextLine().strip().toLowerCase();

            System.out.println(eval(input));
            if (enterGameUi) {
                enterGame();
                enterGameUi = false;
            }
        }
    }


    private String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (authToken == null) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> "exiting the chess app";
                    default -> help();
                };
            }
            return switch (cmd) {
                case "joingame" -> joinGame(params);
                case "listgames" -> listGames(params);
                case "creategame" -> createGame(params);
                case "observegame" -> observeGame(params);
                case "logout" -> logOut(params);
                case "leavegame" -> leaveGame(params);
                case "quit" -> "exiting the chess app";
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
            throw new ResponseException("Invalid amount of arguments");
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
            if (params.length == 3) {
                RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
                RegisterResponse registerResponse = server.register(registerRequest);
                authToken = registerResponse.authToken();
                return String.format("registered and logged in as %s", registerResponse.username());
            }
            throw new ResponseException("Incorrect number of inputs supplied");
    }

    public String logOut(String... params) throws ResponseException {
        if (params.length != 0) {
            throw new ResponseException("Incorrect number of inputs supplied");
        }
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        server.logout(logoutRequest);
        authToken = null;
        return String.format("successfully logged out");
    }

    public String createGame(String... params) {
        if (params.length == 1) {
            CreateGameRequest createGameRequest = new CreateGameRequest(params[0], authToken);
            server.createGame(createGameRequest);
            updateGameList();
            return String.format("created game: %s", createGameRequest.gameName());
        } else {
            throw new ResponseException("Incorrect number of inputs supplied");
        }
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            String color = params[0].toLowerCase().strip();
            String gameKey = params[1];
            int gameID = selectGame(String.valueOf(gameKey)).gameID();

            JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID, authToken);
            server.joinGame(joinGameRequest);
            updateGameList();

            currentChessPerspective = (color.equals("white")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            currentChessGameNo = gameKey;
            enterGameUi = true;

            return String.format("Joined game no. %s, as %s player\n", gameKey, color) + observeGame(gameKey, color);
        }
        throw new ResponseException("Incorrect number of inputs supplied");
    }

    public String leaveGame(String... params) throws ResponseException {
        if (params.length == 1) {
            String gameKey = params[0];
            int gameID = selectGame(String.valueOf(gameKey)).gameID();

            RemovePlayerRequest removePlayerRequest = new RemovePlayerRequest(gameID, authToken);
            RemovePlayerResponse removePlayerResponse = server.leaveGame(removePlayerRequest);
            updateGameList();

            return String.format("%s player %s left game no. %s\n", removePlayerResponse.color(), removePlayerResponse.username(), gameKey);
        }
        throw new ResponseException("Incorrect number of inputs supplied");
    }


    public String listGames(String... params) throws ResponseException {
        if (params.length != 0) {
            throw new ResponseException("Incorrect number of inputs supplied");
        }

        updateGameList();

        String gameListString = "";
        if (gameList.size() == 0) {
            return "\nNo games to list";
        }
        for (String gameKey : gameList.keySet()) {
            GameData game = gameList.get(gameKey);
            String whitePlayer = (game.whiteUsername() != null) ? game.whiteUsername() : "No Player";
            String blackPlayer = (game.blackUsername() != null) ? game.blackUsername() : "No PLayer";
            String gameInfo = String.format("\nGameNumber: %s GameName: %s  WhitePlayer: %s  BlackPlayer: %s", gameKey, game.gameName(), whitePlayer, blackPlayer);
            gameListString = gameListString + gameInfo;
        }
        return gameListString;
    }

    public String observeGame(String... params) throws ResponseException {
        ChessGame.TeamColor teamColor;
        if (params.length != 1 && params.length != 2) {
            throw new ResponseException("Incorrect number of inputs supplied");
        }
        if (params.length == 2) {
            String color = params[1].strip().toLowerCase();
            if (color.equals("white")) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else if (color.equals("black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            } else {
                throw new ResponseException(String.format("your provided color (%s) is invalid. It should be black or white", color));
            }
        } else {
            teamColor = ChessGame.TeamColor.WHITE;
        }
        String gameID = params[0];
        ChessGame chessGame = selectGame(gameID).game();
        return "\n" + drawGameBoard(chessGame, teamColor) + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;
    }

    public String help() {
        if (authToken != null) {
           return """
                    - creategame <gameName>
                    - joingame <playerColor>(white or black) <gameID>
                    - listgames
                    - observegame <gameID>
                    - logout
                    - quit
                    - help (displays possible actions for the user)""";
        }
        return """
                - register <username> <password> <email>
                - login <username> <password>
                - quit
                - help (displays possible actions for the user)""";
    }

    private GameData selectGame(String gameNumber) {
        GameData gameData = gameList.get(gameNumber);
        if (gameData == null) {
            throw new ResponseException("Could not find game. Either the game number is invalid, or you need to run listgames to load the games.");
        }
        return gameData;
    }

    private void updateGameList() {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResponse listGamesResponse = server.listGames(listGamesRequest);
        int gameNumberCounter = 1;
        for (GameData game : listGamesResponse.games()) {
            gameList.put(String.valueOf(gameNumberCounter), game);
            gameNumberCounter++;
        }
    }

    private void enterGame() {
        GameData currentGameData = selectGame(currentChessGameNo);
        GameplayUi gameplayUi = new GameplayUi(serverUrl, currentGameData.game(), currentGameData.gameID(), currentChessPerspective, authToken);
        gameplayUi.open();
    }

}
