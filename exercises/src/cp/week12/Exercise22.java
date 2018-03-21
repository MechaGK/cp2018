package cp.week12;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise22
{
	/*
	- Modify Exercise21 such that you also look for text files with a ".dat" suffix, not just ".txt"
	- When you find a ".dat" file, launch an executor task that computes how many times the letter "b" is present in the file.
	- At the end, the program should print *separately*: The number of "a", and the number of "b".
	*/

	static class Result {
	    private final long a;
	    private final long b;

	    Result(long a, long b) {
	        this.a = a;
	        this.b = b;
        }

        public long getA() {
            return a;
        }

        public long getB() {
            return b;
        }

        @Override
        public String toString() {
            return String.format("a: %d, b: %d", this.a, this.b);
        }
    }

    static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static Result countInDirectory(Path path) {
        List<Future<Result>> futures = new ArrayList<>();

        try {
            Files.list(path).forEach(p -> {
                if (Files.isRegularFile(p)) {
                    if (p.toString().endsWith(".txt")) {
                        futures.add(executorService.submit(() -> {
                            try {
                                return new Result(Files.lines(p)
                                        .mapToLong(l -> l.chars().filter(c -> c == 'a').count())
                                        .sum(), 0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return new Result(0L, 0L);
                        }));
                    } else if (p.toString().endsWith(".dat")) {
                        futures.add(executorService.submit(() -> {
                            try {
                                return new Result(0, Files.lines(p)
                                        .mapToLong(l -> l.chars().filter(c -> c == 'b').count())
                                        .sum());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return new Result(0L, 0L);
                        }));
                    }
                } else if (Files.isDirectory(p)) {
                    futures.add(executorService.submit(() -> countInDirectory(p)));
                }
            });

            return (Result)futures.stream()
                    .map(integerFuture -> {
                        try {
                            return integerFuture.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        return 0L;
                    }).reduce(new Result(0, 0), (a, b) ->
                            new Result(((Result)a).getA() + ((Result)b).getA(), ((Result)a).getB() + ((Result)b).getB()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(0, 0);
    }

    public static void main(String[] args) {
        System.out.println(countInDirectory(Paths.get("/Users/mads/data_example")));
        executorService.shutdown();
    }
}
