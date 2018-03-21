package cp.week12;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise21 {
	/*
	- Modify Exercise20 such that each sub-directory is assigned a dedicated executor task to returns the total of "a" for that sub-directory.
	- The end result must be the same (print the total number for the initial directory on screen).
	*/

    static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static long countAInDirectory(Path path) {
        List<Future<Long>> futures = new ArrayList<>();

        try {
            Files.list(path).forEach(p -> {
                if (Files.isRegularFile(p)) {
                    if (p.toString().endsWith(".txt")) {
                        futures.add(executorService.submit(() -> {
                            try {
                                return Files.lines(p)
                                        .mapToLong(l -> l.chars().filter(c -> c == 'a').count())
                                        .sum();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return 0L;
                        }));
                    }
                } else if (Files.isDirectory(p)) {
                    futures.add(executorService.submit(() -> countAInDirectory(p)));
                }
            });

            return futures.stream()
                    .mapToLong(integerFuture -> {
                        try {
                            return integerFuture.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        return 0L;
                    }).sum();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public static void main(String[] args) {
        System.out.println(countAInDirectory(Paths.get("/Users/mads/data_example")));
        executorService.shutdown();
    }
}
