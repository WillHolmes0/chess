import java.util.Arrays;
import java.util.Scanner;

public class GameplayUi extends UiBase {

    public GameplayUi() {

    }

    public void open() {
        System.out.println("Gameplay Mode Entered");

        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (input != "leave") {
            System.out.printf(">>>");
            input  = scanner.nextLine().strip().toLowerCase();
            //handle input
        }
    }

    private String eval(String input) {
        String[] tokens = input.split(" ");
        String cmd = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            default -> "please enter a valid command";
        };
    }


}
