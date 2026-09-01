import java.io.IOException;
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

            assertInvalidLineNumber(definitions, "0/alpha\n");
            assertInvalidLineNumber(definitions, "-1/alpha\n");
            assertDuplicateTargetLine(definitions);
            assertEmptySignature(definitions);

            Files.write(definitions, "1/alpha\n".getBytes("UTF-8"));
            scanner.readPattern(definitions.toString());
            assertTrue(scanner.containsVirus(infected.toString()), "line 1 must remain a valid target");
        } finally {
            Files.deleteIfExists(definitions);
            Files.deleteIfExists(infected);
            Files.deleteIfExists(clean);
        }
    }

    private static void assertInvalidLineNumber(Path definitions, String definition) throws Exception {
        Files.write(definitions, definition.getBytes("UTF-8"));
        try {
            new AntivirusAlgoInJava().readPattern(definitions.toString());
        } catch (IOException expected) {
            assertTrue(expected.getMessage().contains("definition line 1"),
                    "invalid target errors should identify the definition line");
            return;
        }
        throw new AssertionError("non-positive target line must be rejected: " + definition);
    }

    private static void assertDuplicateTargetLine(Path definitions) throws Exception {
        Files.write(definitions, "1/alpha\n1/omega\n".getBytes("UTF-8"));
        try {
            new AntivirusAlgoInJava().readPattern(definitions.toString());
        } catch (IOException expected) {
            assertTrue(expected.getMessage().contains("definition line 2"),
                    "duplicate target errors should identify the second definition");
            return;
        }
        throw new AssertionError("duplicate target lines must be rejected");
    }

    private static void assertEmptySignature(Path definitions) throws Exception {
        Files.write(definitions, "1/\n".getBytes("UTF-8"));
        try {
            new AntivirusAlgoInJava().readPattern(definitions.toString());
        } catch (IOException expected) {
            assertTrue(expected.getMessage().contains("definition line 1"),
                    "empty signature errors should identify the definition line");
            return;
        }
        throw new AssertionError("empty signatures must be rejected");
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
