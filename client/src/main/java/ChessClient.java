import com.sun.nio.sctp.NotificationHandler;

import java.util.Scanner;

public class ChessClient {

    public ChessClient(String serverUrl) {

    }

    public void run() {
        System.out.println("Welcome to the ChessServer");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            try {
                System.out.println(line);
            } catch (Throwable e) {
                System.out.println("there was an error");
            }
        }
    }

}
