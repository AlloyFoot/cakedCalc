import java.util.Scanner;

public class CakedCalc {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("This is the cakedCalc. Type an expression, or 'quit' to exit.");

        while (true) {
            System.out.print("> ");
            if (!sc.hasNextLine()) break;

            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("quit")) break;

            System.out.println("You typed: " + line);
        }

    }
}