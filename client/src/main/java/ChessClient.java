import com.sun.nio.sctp.NotificationHandler;

import java.util.Scanner;

public class ChessClient {
    private boolean signedIn = true;

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
        if (tokens.length == 1) {
            String cmd = tokens[0];
            return switch (cmd) {
                case "quit" -> "quit";
                default -> help();
            };
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
