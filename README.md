# Java Text Signature Scanner

A small educational Java example that reads line-based text signatures, checks a
second text file for those signatures, and reports the result in a Swing dialog.

Despite the repository name, this is **not a production antivirus or malware
scanner**. It demonstrates file reading, maps, string matching, and a minimal
desktop notification.

## How it works

`Definitions.txt` contains one expected match per line in the form:

```text
line-number/text
```

For example, `4/V` means that line 4 of the scanned file must contain `V`.
The included definitions expect the letters in `VIRUS` on lines 4, 9, 13, 16,
and 21 of `Virus.txt`.

`AntivirusAlgoInJava`:

1. loads the definitions into a `HashMap`;
2. reads the target file line by line;
3. performs a case-sensitive substring check at each defined line number; and
4. displays **Virus Detected** only when every loaded definition matches.

Any missing match produces the **No Virus Found** dialog.

## Technologies

- Java
- Java standard-library file I/O
- Swing `JOptionPane`
- `HashMap` and `Set`

There are no declared external dependencies or build-tool files.

## Repository contents

| File | Purpose |
| --- | --- |
| `Main.java` | Contains the scanner and the `AntivirusAlgoInJava` entry point |
| `Definitions.txt` | Sample line-number and text signatures |
| `Virus.txt` | Sample input that matches all included signatures |

## Requirements

- A Java Development Kit (JDK) with `javac` and `java`
- A desktop environment capable of displaying Swing dialogs

The repository does not specify a minimum JDK version.

## Setup

The current source uses absolute Windows paths from the original development
machine:

```text
C:\Users\og5\IdeaProjects\antivirus\src\definitions.txt
C:\Users\og5\IdeaProjects\antivirus\src\virus.txt
```

Before running, edit the two paths near the end of `Main.java` so they point to
your local copies of `Definitions.txt` and `Virus.txt`.

The checked-in filenames begin with uppercase letters. On case-sensitive
systems, preserve that capitalization when setting the paths.

## Compile and run

From the repository root:

```bash
javac Main.java
java AntivirusAlgoInJava
```

With the included sample files and corrected paths, the target contains every
defined signature and the program displays the detection dialog.

## Manual testing

There is no automated test suite. A focused manual check is:

1. Compile and run with the included sample files; confirm that the detection
   dialog appears.
2. Remove or change one expected character in a copy of `Virus.txt`.
3. Run again; confirm that the clean-file dialog appears.
4. Restore the sample file after testing.

## Screenshot

No application screenshot is stored in the repository. The only interface is a
Swing result dialog, so no placeholder is presented as actual application
output.

## Limitations

- The file paths are hard-coded and there are no command-line arguments or file
  picker.
- Matching is based only on line numbers and case-sensitive substrings.
- The program does not inspect binaries, processes, archives, memory, hashes, or
  known malware formats.
- A file is reported as detected only when every loaded definition matches.
- Errors while loading definitions are currently suppressed; other file errors
  are printed to the console.
- The scanner uses raw collection types and has no automated tests, packaging,
  continuous integration, or release artifacts.
- A clean result means only that the configured text pattern did not fully
  match. It is not a security guarantee.

## Repository status

This repository is best treated as a compact learning exercise rather than a
security product. A future code change could make the file paths portable,
validate malformed definition lines, expose errors clearly, and add automated
tests.
