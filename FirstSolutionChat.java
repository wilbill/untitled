import java.io.IOException;
import java.nio.file.*;
import java.util.regex.*;
import java.util.stream.Stream;

public class FirstSolutionChat {

    public static void main(String[] args) {
        // Root directory containing files
        String rootDir = "../phoneFolder";

        // Regex pattern for phone numbers
        String phoneRegex = "\\d{3}-\\d{3}-\\d{4}|\\(\\d{3}\\) \\d{3}-\\d{4}";
        Pattern pattern = Pattern.compile(phoneRegex);

        try (Stream<Path> paths = Files.walk(Paths.get(rootDir))) {
            // Filter only regular files
            paths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    // Read all lines from the file
                    String content = Files.readString(file);

                    // Check if the file contains the phone number
                    Matcher matcher = pattern.matcher(content);
                    if (matcher.find()) {
                        System.out.println("File containing phone number: " + file);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file + " - " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error walking through directory: " + e.getMessage());
        }
    }
}
