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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private String authToken = null;
    private String playerName;
    private ServerFacade server;
    private ArrayList<GameData> gameList = new ArrayList<>();

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to the ChessServer");

        //chessboard display for testing
        //login("will", "passy");
//        listGames();
//        String observableGame = observeGame("9764");
//        System.out.print(observableGame);
        //end testing code

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("\n>>> ");
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
                return String.format("registered as %s", registerResponse.username());
            }
            throw new ResponseException("Incorrect number of inputs supplied");
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
            return String.format("created game: %s  GameID is %d", createGameRequest.gameName(), createGameResponse.gameID());
        } else {
            throw new ResponseException("Incorrect number of inputs supplied");
        }
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            String color = params[0].toLowerCase().strip();
            int gameID = Integer.parseInt(params[1]);
            JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID, authToken);
            server.joinGame(joinGameRequest);
            return String.format("Joined game no. %s, as %s player", gameID, color);
        }
        throw new ResponseException("Incorrect number of inputs supplied");
    }

    public String listGames() throws ResponseException {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResponse listGamesResponse = server.listGames(listGamesRequest);
        gameList = listGamesResponse.games();

        String gameList = "";
        int counter = 1;
        if (listGamesResponse.games().size() == 0) {
            return "\nNo games to list";
        }
        for (GameData game : listGamesResponse.games()) {
            String whitePlayer = (game.whiteUsername() != null) ? game.whiteUsername() : "No Player";
            String blackPlayer = (game.blackUsername() != null) ? game.blackUsername() : "No PLayer";
            String gameInfo = String.format("\n%d. GameName: %s  GameID; %s  WhitePlayer: %s  BlackPlayer: %s", counter, game.game(), game.gameID(), whitePlayer, blackPlayer);
            gameList = gameList + gameInfo;
        }
        return gameList;
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException("Incorrect number of inputs supplied");
        }
        String gameID = params[0];
        ChessGame chessGame = selectGame(gameID);
        if (chessGame == null) {
            throw new ResponseException("Could not find game. Either the game ID is invalid, or you need to run listgames to load the games.");
        }
        return "\n" + drawGameBoard(chessGame, ChessGame.TeamColor.WHITE) + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;
    }

    public String help() {
        if (authToken != null) {
           return """
                    - creategame <gameName>
                    - joingame <playerColor>(white or black) <gameID>
                    - listgames
                    - observegame <gameID>
                    - logout
                    - quit""";
        }
        return """
                - register <username> <password> <email>
                - login <username> <password>
                - quit""";
    }

    private ChessGame selectGame(String gameID) {
        for (GameData gameOption : gameList) {
            if (gameID.equals(String.valueOf(gameOption.gameID()))) {
                return gameOption.game();
            }
        }
        return null;
    }

    private String drawGameBoard(ChessGame chessGame, ChessGame.TeamColor perspective) {
        ChessBoard chessBoard = chessGame.getBoard();
        String chessBoardString = "";
        int square = 0;
        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int i = 9; i >= 0; i--) {
                for (int j = 0; j <= 9; j++) {
                    if (j == 0) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        if (i == 9 || i == 0) {
                            chessBoardString += "   ";
                        } else {
                            chessBoardString += "  " + String.valueOf(i) + " ";
                        }
                    } else if (j == 9) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        if (i == 9 || i == 0) {
                            chessBoardString += "  ";
                        } else {
                            chessBoardString += "  " + String.valueOf(i) + " ";
                        }
                    } else if (i == 9 || i == 0) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        int asciiCode = 0x40 + j;
                        chessBoardString += String.format(" %c  ", asciiCode);
                    } else {
                        if (square % 2 == 1) {
                            chessBoardString += (EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        } else {
                            chessBoardString += (EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        }
                        ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(i, j));
                        chessBoardString += getPieceString(chessPiece);
                    }
                    square++;
                }
                square++;
                chessBoardString += (EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.RESET_BG_COLOR + "\n");
            }
        } else {
            for (int i = 0; i <= 9; i++) {
                for (int j = 9; j >= 0; j--) {
                    if (j == 0) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        if (i == 9 || i == 0) {
                            chessBoardString += "  ";
                        } else {
                            chessBoardString += "  " + String.valueOf(i) + " ";
                        }
                    } else if (j == 9) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        if (i == 9 || i == 0) {
                            chessBoardString += "   ";
                        } else {
                            chessBoardString += "  " + String.valueOf(i) + " ";
                        }
                    } else if (i == 9 || i == 0) {
                        chessBoardString += EscapeSequences.SET_BG_COLOR_BLACK;
                        chessBoardString += EscapeSequences.SET_TEXT_COLOR_WHITE;
                        int asciiCode = 0x40 + j;
                        chessBoardString += String.format(" %c  ", asciiCode);
                    } else {
                        if (square % 2 == 1) {
                            chessBoardString += (EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                        } else {
                            chessBoardString += (EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        }
                        ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(i, j));
                        chessBoardString += getPieceString(chessPiece);
                    }
                    square++;
                }
                square++;
                chessBoardString += (EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.RESET_BG_COLOR + "\n");
            }
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
