import java.util.Scanner;

public class CakedCalc {

    private final String src;
    private int pos = 0;
    private static double lastAnswer = 0;
    private static final String[][] WORD_FUNCTIONS = {
        {"square root of", "sqrt"},
        {"absolute value of", "abs"},
        {"sine of", "sin"},
        {"cosine of", "cos"},
        {"tangent of", "tan"},
        {"natural log of", "ln"},
        {"log of", "log"},
    };

    private CakedCalc(String src) {
        this.src = src;
    }

        private static String normalize(String s) {
        s = s.toLowerCase().trim();
        s = s.replaceAll("^(what is|what's|calculate|compute|solve)\\s+", "");
        s = s.replaceAll("^the\\s+", "");
        s = s.replace("?", "").replace("=", "");

        for (String[] f : WORD_FUNCTIONS) {
            s = s.replaceAll("\\b" + f[0] + "\\s+(-?[0-9.]+|pi|e|ans|\\([^()]*\\))(?![a-z0-9.])", f[1] + "($1)");
        }
        s = s.replaceAll("\\bplus\\b", "+");
        s = s.replaceAll("\\bminus\\b", "-");
        s = s.replaceAll("\\b(times|multiplied by)\\b", "*");
        s = s.replaceAll("\\bdivided by\\b", "/");
        s = s.replaceAll("\\bto the power of\\b", "^");
        s = s.replaceAll("\\b(mod|modulo)\\b", "%");
        s = s.replaceAll("\\bsquared\\b", "^2");
        s = s.replaceAll("\\bcubed\\b", "^3");
        return s;
    }

    public static double calculate(String input) {
        CakedCalc c = new CakedCalc(normalize(input));
        double result = c.parseExpression();
        c.skipSpaces();
        if (c.pos < c.src.length()) {
            throw new IllegalArgumentException("Unexpected '" + c.src.charAt(c.pos) + "' at position " + c.pos);
        }
        if (!Double.isNaN(result) && !Double.isInfinite(result)) {
            lastAnswer = result;
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
            } else if (startsImplicitFactor()) {
                value *= parseUnary();
            } else {
                return value;
            }
        }
    }

    private double parseUnary() {
        if (match('+')) return parseUnary();
        if (match('-')) return -parseUnary();
        return parsePower();
    }

    private double parsePower() {
        double base = parsePrimary();
        if (match('^')) {
            double exponent = parseUnary();
            base = Math.pow(base, exponent);
        }
        return base;
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

        if (pos < src.length() && Character.isLetter(src.charAt(pos))) {
            int start = pos;
            while (pos < src.length() && Character.isLetter(src.charAt(pos))) pos++;
            String name = src.substring(start, pos).toLowerCase();

            if (name.equals("pi")) return Math.PI;
            if (name.equals("e")) return Math.E;
            if (name.equals("ans")) return lastAnswer;

            if (!match('(')) throw new IllegalArgumentException("Unknown name: " + name);
            double arg = parseExpression();
            if (!match(')')) throw new IllegalArgumentException("Missing closing parenthesis after " + name);
            return applyFunction(name, arg);
        }

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

    private double applyFunction(String name, double arg) {
        switch (name) {
            case "sin": return Math.sin(Math.toRadians(arg));
            case "cos": return Math.cos(Math.toRadians(arg));
            case "tan": return Math.tan(Math.toRadians(arg));
            case "asin": return Math.toDegrees(Math.asin(arg));
            case "acos": return Math.toDegrees(Math.acos(arg));
            case "atan": return Math.toDegrees(Math.atan(arg));
            case "sqrt": return Math.sqrt(arg);
            case "log": return Math.log10(arg);
            case "ln": return Math.log(arg);
            case "exp": return Math.exp(arg);
            case "abs": return Math.abs(arg);
            default: throw new IllegalArgumentException("Unknown function: " + name);
        }
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

    private boolean startsImplicitFactor() {
        skipSpaces();
        if (pos >= src.length()) return false;
        char c = src.charAt(pos);
        return c == '(' || Character.isLetter(c);
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