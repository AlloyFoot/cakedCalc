import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

// Lets the web demo run CakedCalc's real main() in the browser.
// Browsers can't type into System.in, so the page sends every line typed so far,
// this replays them through main() and returns everything main() printed.
public class DemoBridge {
    public static String run(String input) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        try {
            CakedCalc.clearAnswer(); // start each replay from a fresh ans
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
            CakedCalc.main(new String[0]);
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
        return captured.toString(StandardCharsets.UTF_8);
    }
}
