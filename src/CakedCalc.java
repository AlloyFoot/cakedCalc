import java.util.Scanner;

public class CakedCalc {

    private final String src;
    private int pos = 0;

    private CakedCalc(String src) {
        this.src = src;
    }

    public static double calculate(String input) {
        CakedCalc c = new CakedCalc(input);
        double result = c.parseExpression();
        c.skipSpaces();
        if (c.pos < c.src.length()) {
            throw new IllegalArgumentException("Unexpected '" + c.src.charAt(c.pos) + "' at position " + c.pos);
        }
        return result;
    }

    private double parseExpression() {
       double value = parseTerm();
       while (true) {
            if (match('+')) value += parseTerm();
            else if (match('-')) value -= parseTerm();
            else return value;
        }
    }

    private double parseTerm() {
        double value = parseUnary();
        while (true) {
            if (match('*')) {
                value *= parseUnary();
            } else if (match('/')) {
                double d = parseUnary();
                if (d == 0) throw new ArithmeticException("Division by zero");
                value /= d;
            } else if (match('%')) {
                double d = parseUnary();
                if (d == 0) throw new ArithmeticException("Modulo by zero");
                value %= d;
            } else {
                return value;
            }
        }
    }

    private double parseUnary() {
        if (match('+')) return parseUnary();
        if (match('-')) return -parseUnary();
        return parsePrimary();
    }

    private double parsePrimary() {
        if (match('(')) {
            double value = parseExpression();
            if (!match(')')) {
                throw new IllegalArgumentException("Expected ')' at position " + pos);
            }
            return value;
        }

        skipSpaces();
        int start = pos;
        while (pos < src.length()
                && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.')) {
            pos++;
        }
        if (start == pos) {
            throw new IllegalArgumentException("Expected a number at position " + pos);
        }
        return Double.parseDouble(src.substring(start, pos));
    }

    private void skipSpaces() {
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
    }

    private boolean match(char expected) {
        skipSpaces();
        if (pos < src.length() && src.charAt(pos) == expected) {
            pos++;
            return true;
        }
        return false;
    }

    private static String format(double d) {
        if (d == Math.rint(d) && Math.abs(d) < 1e15) return String.valueOf((long) d);
        return String.valueOf(d);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("This is the cakedCalc. Type an expression, or 'quit' to exit.");

        while (true) {
            System.out.print("> ");
            if (!sc.hasNextLine()) break;

            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("quit")) break;

            try {
                System.out.println(format(calculate(line)));
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        sc.close();
    }


    
}