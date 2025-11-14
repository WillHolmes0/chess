import com.sun.nio.sctp.NotificationHandler;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private boolean signedIn = false;
    private String playerName;

    public ChessClient(String serverUrl) {

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
                System.out.println("there was an error");
            }
        }
    }

    private String eval(String input) {
        String[] tokens = input.toLowerCase().strip().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "signin" -> signIn(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String signIn(String... params) {
        if (params.length >= 1) {
            signedIn = true;
            playerName = String.join(" ", params);
            return String.format("You signed in as %s.", playerName);
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
                - signIn <yourname>
                - quit
               """;
    }


}
