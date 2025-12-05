import chess.ChessGame;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Scanner;

public class GameplayUi extends UiBase {
    private int chessGameID;
    private WebSocketFacade webSocketFacade;
    private ChessGame.TeamColor color;

    public GameplayUi(String serverUrl, int chessGameID, ChessGame.TeamColor color) {
        this.chessGameID = chessGameID;
        this.color = color;
        webSocketFacade = new WebSocketFacade(serverUrl);
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
            default -> "please enter a valid command";
        };
    }

    private String leave() {
        return "leaving the game";
    }


}
