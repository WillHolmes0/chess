import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import requests.*;
import responses.*;
import server.ResponseException;
import server.ServerFacade;
import ui.EscapeSequences;

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

        // testing sequence
//        login("willh", "passy");
//        ChessGame game = selectGame("3966");
//        drawGameBoard(game);

        // end testing sequence

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
                case "observegame" -> observeGame(params);
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
            String whitePlayer = (game.whiteUsername() != null) ? game.whiteUsername() : "No Player";
            String blackPlayer = (game.blackUsername() != null) ? game.blackUsername() : "No PLayer";
            String gameInfo = String.format("%d. GameName: %s  GameID; %s  WhitePlayer: %s  BlackPlayer: %s\n", counter, game.game(), game.gameID(), whitePlayer, blackPlayer);
            gameList = gameList + gameInfo;
        }
        return gameList;
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException("Error: incorrect number of inputs supplied");
        }
        String gameID = params[0];
        ChessGame chessGame = selectGame(gameID);
        if (chessGame == null) {
            throw new ResponseException("Error: invalid gameID");
        }
        ChessGame game = selectGame("3966");
        return "\n" + drawGameBoard(game) + EscapeSequences.SET_BG_COLOR_DARK_GREY + "\n";
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
                   """;
        }
        return """
                - register <username> <password> <email>
                - login <username> <password>
                - quit
               """;
    }

    private ChessGame selectGame(String gameID) {
        GameData game;
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResponse listGamesResponse = server.listGames(listGamesRequest);
        for (GameData gameOption : listGamesResponse.games()) {
            if (gameID.equals(String.valueOf(gameOption.gameID()))) {
                return gameOption.game();
            }
        }
        return null;
    }

    private String drawGameBoard(ChessGame chessGame) {
        ChessBoard chessBoard = chessGame.getBoard();
        String chessBoardString = "";
        int square = 0;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (square % 2 == 0) {
                    chessBoardString += (EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                } else {
                    chessBoardString += (EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }
                ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(i, j));
                chessBoardString += getPieceString(chessPiece);
                square++;
            }
            square--;
            chessBoardString += (EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        }
        return chessBoardString;
    }

    private String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        String chessPieceString = "";
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            chessPieceString += EscapeSequences.SET_TEXT_COLOR_WHITE;
        } else {
            chessPieceString += EscapeSequences.SET_TEXT_COLOR_BLACK;
        }
        chessPieceString += switch (piece.getPieceType()) {
            case ChessPiece.PieceType.ROOK -> EscapeSequences.BLACK_ROOK;
            case ChessPiece.PieceType.KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case ChessPiece.PieceType.BISHOP -> EscapeSequences.BLACK_BISHOP;
            case ChessPiece.PieceType.KING -> EscapeSequences.BLACK_KING;
            case ChessPiece.PieceType.PAWN -> EscapeSequences.BLACK_PAWN;
            case ChessPiece.PieceType.QUEEN -> EscapeSequences.BLACK_QUEEN;
        };
        return chessPieceString;
    }

}
