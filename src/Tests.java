public class Tests {
    
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String input, double expected) {
        try {
            double actual = CakedCalc.calculate(input);
            if (Math.abs(actual - expected) < 1e-9) {
                passed++;
                System.out.println("PASS " + input + " -> " + actual);
            } else {
                failed++;
                System.out.println("FAIL " + input + " expected " + expected + " but got " + actual);
            }
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL " + input + " -> Exception: " + e.getMessage());
        }
    }

    private static void checkError(String input, String messagePart) {
        try { 
            double actual = CakedCalc.calculate(input);
            failed++;
            System.out.println("FAIL " + input + " expected error containing '" + messagePart + "' but got result " + actual);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains(messagePart)) {
                passed++;
                System.out.println("PASS " + input + " -> error: " + e.getMessage());
            } else {
                failed++;
                System.out.println("FAIL " + input + " wrong error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Basic arithmetic
        check("2 + 3", 5);
        check("10 - 4 + 1", 7);
        check("1.5 + 2.25", 3.75);

        // Precedence
        check("2 + 3 * 4", 14);
        check("10 - 6 / 2", 7);
        check("2 * 3 + 4 * 5", 26);
        check("10 / 4", 2.5);
        check("10 % 3", 1);

        // Unary minus and parentheses
        check("-5 + 2", -3);
        check("3 * -2", -6);
        check("3 - -2", 5);
        check("(2 + 3) * 4", 20);
        check("2 * (3 + 4)", 14);
        check("-(2 + 3)", -5);
        check("((1 + 2) * (3 + 4))", 21);

        // Powers, functions, constants
        check("2 ^ 3", 8);
        check("2 ^ 3 ^ 2", 512);
        check("-2 ^ 2", -4);
        check("2 ^ -1", 0.5);
        check("sqrt(16)", 4);
        check("abs(-5)", 5);
        check("sqrt(9) + 2 * 3", 9);
        check("2 * pi", 2 * Math.PI);
        check("sin(90)", 1);
        check("sin(30)", 0.5);
        check("log(1000)", 3);
        check("ln(e)", 1);

        // Plain English
        check("what is 12 plus 8 times 2", 28);
        check("15 divided by 4", 3.75);
        check("10 minus 3", 7);
        check("2 to the power of 10", 1024);
        check("7 mod 3", 1);
        check("5 squared", 25);
        check("3 cubed", 27);
        check("Calculate 6 TIMES 7?", 42);
        check("what is sqrt(16) plus 1", 5);

        // Function phrases
        check("what is the square root of 16?", 4);
        check("square root of 16 plus 9", 13);
        check("what is the absolute value of -5", 5);
        check("log of 1000", 3);
        check("natural log of e", 1);
        check("the square root of (16 + 9)", 5);

        // Implicit multiplication
        check("2pi", 2 * Math.PI);
        check("2 pi", 2 * Math.PI);
        check("-2pi", -2 * Math.PI);
        check("2e", 2 * Math.E);
        check("2(3 + 4)", 14);
        check("(1 + 2)(3 + 4)", 21);
        check("2(3)(4)", 24);
        check("2sqrt(16)", 8);
        check("3 sqrt(16)", 12);
        check("sin(30)cos(60)", 0.25);
        check("2 * 3pi", 6 * Math.PI);

        // ans (each result becomes the next "ans", so order matters)
        check("6 * 7", 42);
        check("ans + 1", 43);
        check("ans * 2", 86);
        check("2ans", 172);
        check("ans", 172);
        check("100", 100);
        check("ANS / 4", 25);
        check("square root of ans", 5);
        check("ans ^ 2", 25);
        checkError("ans / 0", "Division by zero");
        check("ans", 25);   // the failed calculation must not overwrite ans

        // Percent phrases
        check("20% of 50", 10);
        check("20 percent of 50", 10);
        check("20 % of 50", 10);
        check("12.5% of 200", 25);
        check("150% of 40", 60);
        check("50 + 10% of 50", 55);
        check("100 - 20% of 100", 80);
        check("(10 + 10)% of 50", 10);
        check("what is 15% of 80?", 12);
        check("10% of 50 + 5", 10);
        check("-20% of 50", -10);
        check("50-20% of 50", 40);
        check("20% of 50% of 100", 10);
        check("200", 200);
        check("10% of ans", 20);
        check("ans% of 50", 10);

        // clear resets ans to 0
        check("42", 42);
        CakedCalc.clearAnswer();
        check("ans", 0);

        // Errors
        checkError("5 / 0", "Division by zero");
        checkError("5 % 0", "Modulo by zero");
        checkError("(2 + 3", "Expected ')'");
        checkError("2 +", "Expected a number");
        checkError(")", "Expected a number");
        checkError("2 + 3 )", "Unexpected");
        checkError("foo(2)", "Unknown function");
        checkError("foo", "Unknown name");
        checkError("2 3", "Unexpected");
        checkError("2(", "Expected a number");
        checkError("20% of", "Expected a number");
        checkError("% of 50", "Expected a number");
        checkError("help", "Unknown name");
        checkError("history", "Unknown name");
        checkError("clear", "Unknown name");

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }
}
