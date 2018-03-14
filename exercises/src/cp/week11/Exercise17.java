package cp.week11;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise17 {
	/*
	- Modify Exercise 16 such that:
		* There are a few consumer threads running in parallel to the
		  file finder.
		* Each consumer consumes a Path object from the shared blocking deque
		  at a time.
		* When a consumer consumes a Path, it prints on screen the content of
	      the file.
	*/

    public static void fileFinder(Path path, BlockingDeque<Path> deque) {
        try {
            Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(subPath -> subPath.toString().endsWith(".txt"))
                    .forEach(deque::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * An lazy as in lazy developer solution. Thread safety is jeopardized by only giving producers a small time frame
     * to adding products to deque
     */
    public static void consume(BlockingDeque<Path> deque) {

        while (!Thread.interrupted()) {
            try {
                Path path = deque.poll(5, TimeUnit.SECONDS);
                if (path == null) {
                    break;
                }

                System.out.println(Files.readAllLines(path));
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        BlockingDeque<Path> deque = new LinkedBlockingDeque<>();

        fileFinder(Paths.get("/Users/mads/data_example"), deque);

        Thread thread1 = new Thread(() -> consume(deque));
        Thread thread2 = new Thread(() -> consume(deque));
        Thread thread3 = new Thread(() -> consume(deque));

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(deque);
    }
}
