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
        chessGame = loadGameMessage.game();
        drawGameBoard(chessGame, color);
    }


    public void open() {
        System.out.println("Gameplay Mode Entered");
        webSocketFacade.connect(chessGameID, authentication);

        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("leave")) {
            System.out.printf("\n>>> ");
            input = scanner.nextLine().strip().toLowerCase();
            eval(input);
        }
    }

    private void eval(String input) {
        String[] tokens = input.split(" ");
        String cmd = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (cmd) {
            case "leave" -> leave();
            case "redraw" -> redrawBoard();
            case "makemove" -> makeMove(params);
            case "highlightmoves" -> highlightMoves(params);
            case "resign" -> resign();
            default -> help();
        };
    }

    private void help() {
        System.out.println(
            """
            leave - leaves the game
            redraw - redraws the gameboard
            makemove <cord1, cord2> makes a move in the game
            showmoves <cord1> - highlights valid moves for a given piece
            resign - admits defeats and ends the game
            help - displays user options
            """
        );
    }

    private void leave() {
        webSocketFacade.leave(chessGameID, authentication);
    }

    private void resign() {
        webSocketFacade.resign(chessGameID, authentication);
    }

    private void highlightMoves(String... params) {
        if (params.length == 1) {
            String[] coordinates = params[0].split("");
            ChessPosition chessPosition = new ChessPosition(Integer.valueOf(coordinates[1]), evaluateCoordinate(coordinates[0]));
            Collection<ChessMove> validMoves = chessGame.validMoves(chessPosition);
            Collection<ChessPosition> validPositions = new ArrayList<>();
            validMoves.forEach((move) -> validPositions.add(move.getEndPosition()));
            System.out.println(drawGameBoard(chessGame, color, validPositions));
        }
        throw new ResponseException("Error: incorrect number of parameters for the given command");
    }

    private void redrawBoard() {
        System.out.println(drawGameBoard(chessGame, color));
    }

    private void makeMove(String... params) {
        if (params.length == 2) {
            String[] initialCords = params[0].split("");
            String[] finalCords = params[1].split("");
            int y1 = evaluateCoordinate(initialCords[0]);
            int x1 = Integer.valueOf(initialCords[1]);
            int y2 = evaluateCoordinate(finalCords[0]);
            int x2 = Integer.valueOf(finalCords[1]);
            ChessMove chessMove = new ChessMove(new ChessPosition(x1, y1), new ChessPosition(x2, y2), null);
            webSocketFacade.makeMove(chessMove, chessGameID, authentication);
        } else {
            throw new ResponseException("Error: incorrect number of parameters for the given command");
        }
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
