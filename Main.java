import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

class AntivirusAlgoInJava {
    private final Map<Integer, String> signatures = new HashMap<>();

    void readPattern(String filename) throws IOException {
        signatures.clear();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            String line;
            int definitionLine = 0;
            while ((line = reader.readLine()) != null) {
                definitionLine++;
                int separator = line.indexOf('/');
                if (separator <= 0) {
                    throw new IOException("Invalid definition at line " + definitionLine);
                }

                try {
                    int targetLine = Integer.parseInt(line.substring(0, separator));
                    if (targetLine <= 0) {
                        throw new IOException("Target line must be positive at definition line " + definitionLine);
                    }
                    signatures.put(targetLine, line.substring(separator + 1));
                } catch (NumberFormatException exception) {
                    throw new IOException("Invalid line number at definition line " + definitionLine, exception);
                }
            }
        }
    }

    boolean containsVirus(String filename) throws IOException {
        int matchedSignatures = 0;
        int currentLine = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                currentLine++;
                String signature = signatures.get(currentLine);
                if (signature != null && line.contains(signature)) {
                    matchedSignatures++;
                }
            }
        }

        return !signatures.isEmpty() && matchedSignatures == signatures.size();
    }

    void searchVirus(String filename) throws IOException {
        if (containsVirus(filename)) {
            JOptionPane.showMessageDialog(null, "Error", "Virus Detected ", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Clean File", "No Virus Found ", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // The scanner can still run with the platform default look and feel.
        }

        Path definitions = args.length > 0 ? Paths.get(args[0]) : Paths.get("Definitions.txt");
        Path fileToScan = args.length > 1 ? Paths.get(args[1]) : Paths.get("Virus.txt");

        try {
            AntivirusAlgoInJava scanner = new AntivirusAlgoInJava();
            scanner.readPattern(definitions.toString());
            scanner.searchVirus(fileToScan.toString());
        } catch (Exception exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Scan failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
