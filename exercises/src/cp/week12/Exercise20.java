package cp.week12;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise20 {
	/*
	- Write a method that finds all text files (files with a ".txt" suffix) in a given directory.
	- Whenever a text file is found, launch a task in an executor and get a future for the task's result.
	- The task should compute how many times the letter "a" is present in the file, and return it as an integer.
	- The main thread should wait for all futures to complete and print on screen the sum of all results.
	*/

    static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static long fileFinder(Path path) {
        List<Future<Long>> futures;

        try {
            futures = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(subPath -> subPath.toString().endsWith(".txt"))
                    .map(p -> {
                        return executorService.submit(() -> {
                            try {
                                return Files.lines(p)
                                        .mapToLong(l -> l.chars().filter(c -> c == 'a').count())
                                        .sum();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return 0L;
                        });
                    }).collect(Collectors.toList());

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
        System.out.println(fileFinder(Paths.get("/Users/mads/data_example")));
        executorService.shutdown();
    }
}
