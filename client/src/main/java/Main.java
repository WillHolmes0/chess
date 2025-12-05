import chess.*;

public class Main {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new ChessClient(serverUrl).run();
//        try {
//            new ChessClient(serverUrl).run();
//        } catch (Throwable e) {
//            System.out.printf("Could not start server: %s%n", e.getMessage());
//        }
    }
}