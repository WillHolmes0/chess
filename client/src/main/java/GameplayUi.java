import chess.ChessGame;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Scanner;

public class GameplayUi extends UiBase {
    private int chessGameID;
    private ChessGame chessGame;
    private WebSocketFacade webSocketFacade;
    private ChessGame.TeamColor color;
    private String authentication;

    public GameplayUi(String serverUrl, ChessGame chessGame, int chessGameID, ChessGame.TeamColor color, String authentication) {
        this.chessGameID = chessGameID;
        this.color = color;
        webSocketFacade = new WebSocketFacade(serverUrl);
        this.authentication = authentication;
        this.chessGame = chessGame;
    }

    public void open() {
        System.out.println("Gameplay Mode Entered");

        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (input != "leave") {
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
            default -> "please enter a valid command";
        };
    }

    private String leave() {
        webSocketFacade.leave(chessGameID, authentication);
        return "left game on ui";
    }

    private String redrawBoard() {
        return drawGameBoard(chessGame, color);
    }
}
