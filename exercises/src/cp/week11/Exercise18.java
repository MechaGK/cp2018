package cp.week11;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise18
{
	/*
	- Modify Exercise 17 such that:
		* When a consumer terminates, it prints on screen the sum of the lengths
		  of all files it read.
	*/

    // Using a poison pill for telling consumers to stop
    private final static Path POSION_PILL = Paths.get("POISON_PILL");

    public static void fileFinder(Path path, BlockingDeque<Path> deque) {
        try {
            Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(subPath -> subPath.toString().endsWith(".txt"))
                    .forEach(deque::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        deque.add(POSION_PILL);
    }

    public static void consume(BlockingDeque<Path> deque) {
        int charactersRead = 0;
        while (!Thread.interrupted()) {
            try {
                // Take is blocking
                Path path = deque.take();

                if (path == POSION_PILL) {
                    deque.add(path);
                    break;
                }

                List<String> lines = Files.readAllLines(path);
                System.out.println(lines);

                // Number of charaters in file
                // charactersRead += lines.stream().mapToInt(String::length).sum();

                // Size of file
                charactersRead += Files.size(path);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.println("I read " + charactersRead + " characters");
    }

    public static void main(String[] args) {
        BlockingDeque<Path> deque = new LinkedBlockingDeque<>();

        fileFinder(Paths.get("/Users/mads/data_example"), deque);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Using a executor service to avoid .starts()s and .join()s
        executorService.submit(() -> consume(deque));
        executorService.submit(() -> consume(deque));
        executorService.submit(() -> consume(deque));
        executorService.submit(() -> consume(deque));

        executorService.shutdown();
    }
}
