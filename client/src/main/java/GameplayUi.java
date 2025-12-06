import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import server.ResponseException;
import websocket.WebSocketFacade;
import websocket.WebSocketMessageHandler;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class GameplayUi extends UiBase implements WebSocketMessageHandler {
    private int chessGameID;
    private ChessGame chessGame;
    private WebSocketFacade webSocketFacade;
    private ChessGame.TeamColor color;
    private String authentication;

    public GameplayUi(String serverUrl, ChessGame chessGame, int chessGameID, ChessGame.TeamColor color, String authentication) {
        this.chessGameID = chessGameID;
        this.color = color;
        webSocketFacade = new WebSocketFacade(serverUrl, this);
        this.authentication = authentication;
        this.chessGame = chessGame;
    }

    public void handleMessage(String message) {
        ServerMessage.ServerMessageType serverMessageType = new Gson().fromJson(message, ServerMessage.class).getServerMessageType();
        switch (serverMessageType) {
            case ServerMessage.ServerMessageType.NOTIFICATION -> handleNotificationMessage(message);
            case ServerMessage.ServerMessageType.LOAD_GAME ->  handleLoadGameMessage(message);
        }
    }

    public void handleNotificationMessage(String message) {
        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
        System.out.println(notificationMessage.getMessage());

    }

    public void handleLoadGameMessage(String message) {
        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        chessGame = loadGameMessage.getGame();
        drawGameBoard(chessGame, color);
    }


    public void open() {
        System.out.println("Gameplay Mode Entered");

        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("leave")) {
            System.out.printf("\n>>> ");
            input = scanner.nextLine().strip().toLowerCase();
            System.out.println(eval(input));
        }
    }

    private String eval(String input) {
        String[] tokens = input.split(" ");
        String cmd = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "leave" -> leave();
            case "redraw" -> redrawBoard();
            case "makemove" -> makeMove(params);
            case "highlightmoves" -> highlightMoves(params);
            default -> "please enter a valid command";
        };
    }

    private String leave() {
        webSocketFacade.leave(chessGameID, authentication);
        return "left game on ui";
    }

    private String highlightMoves(String... params) {
        if (params.length == 1) {
            String[] coordinates = params[0].split("");
            ChessPosition chessPosition = new ChessPosition(Integer.valueOf(coordinates[1]), evaluateCoordinate(coordinates[0]));
            Collection<ChessMove> validMoves = chessGame.validMoves(chessPosition);
            Collection<ChessPosition> validPositions = new ArrayList<>();
            validMoves.forEach((move) -> validPositions.add(move.getEndPosition()));
            return drawGameBoard(chessGame, color, validPositions);
        }
        throw new ResponseException("Error: incorrect number of parameters for the given command");
    }

    private String redrawBoard() {
        return drawGameBoard(chessGame, color);
    }

    private String makeMove(String... params) {
        if (params.length == 2) {
            String[] initialCords = params[0].split("");
            String[] finalCords = params[1].split("");
            int y1 = evaluateCoordinate(initialCords[0]);
            int x1 = Integer.valueOf(initialCords[1]);
            int y2 = evaluateCoordinate(finalCords[0]);
            int x2 = Integer.valueOf(finalCords[1]);
            System.out.println(String.format("Chessmove Cords %s,%s %s,%s", x1, y1, x2, y2));
            ChessMove chessMove = new ChessMove(new ChessPosition(x1, y1), new ChessPosition(x2, y2), null);
            webSocketFacade.makeMove(chessMove, chessGameID, authentication);
        }
        throw new ResponseException("Error: incorrect number of parameters for the given command");
    }

    private int evaluateCoordinate(String input) {
        return switch (input) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new ResponseException("Error: incorrect coordinate inputted");
        };
    }
}
