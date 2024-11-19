
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BestSolutionMultiThreading {
    public static void main(String[] args) {
        String regex = "\\(?\\d{3}\\)?[- ]?\\d{3}[- ]?\\d{4}"; //this regex
        Pattern pattern = Pattern.compile(regex);
        // String directoryPath = "C:/amazon";    Windows based path
        String directoryPath = "../phoneFolder";

        //this executor framework
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Queue<Path> queue = new LinkedList<>();
        queue.add(Paths.get(directoryPath));

        while (!queue.isEmpty()) {
            Path path = queue.poll();

            if (Files.isDirectory(path)) {
                try (Stream<Path> children = Files.list(path)) {
                    children.forEach(queue::add);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                executor.submit(() -> {
                    try (Stream<String> lines = Files.lines(path)) {
                        lines.forEach(line -> {
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                System.out.println("Phone number found in: " + path.toString());
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}