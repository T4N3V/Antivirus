import java.nio.file.Files;
import java.nio.file.Path;

public class AntivirusAlgoInJavaTest {
    public static void main(String[] args) throws Exception {
        Path definitions = Files.createTempFile("definitions", ".txt");
        Path infected = Files.createTempFile("infected", ".txt");
        Path clean = Files.createTempFile("clean", ".txt");

        try {
            Files.write(definitions, "1/alpha\n3/omega\n".getBytes("UTF-8"));
            Files.write(infected, "alpha marker\nmiddle\nomega marker\n".getBytes("UTF-8"));
            Files.write(clean, "alpha marker\nmiddle\nmissing marker\n".getBytes("UTF-8"));

            AntivirusAlgoInJava scanner = new AntivirusAlgoInJava();
            scanner.readPattern(definitions.toString());

            assertTrue(scanner.containsVirus(infected.toString()), "all signatures should be detected");
            assertFalse(scanner.containsVirus(clean.toString()), "a missing signature should produce a clean result");
            assertTrue(scanner.containsVirus(infected.toString()), "repeated scans must not retain counters");
        } finally {
            Files.deleteIfExists(definitions);
            Files.deleteIfExists(infected);
            Files.deleteIfExists(clean);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }
}
