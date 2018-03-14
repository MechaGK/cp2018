package cp.week11;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Phaser;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise19 {
	/*
	- Modify Exercise 18 such that:
		* If a file starts with the string "SKIP", the consumer does not process
		  it.
		* If a file starts with the string "TERM", the consumer terminates
		  immediately (printing on screen the sum of the lengths of all files
		  visited so far).
		* If a consumer terminates because of "TERM", then the consumer starts
		  another consumer.
	
	- Make sure that all consumers terminate when the shared blocking deque is
	  empty and no more files will be added to it.
	*/

    public static void fileFinder(Path path, BlockingDeque<Path> deque, CountDownLatch latch) {
        try {
            Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(subPath -> subPath.toString().endsWith(".txt"))
                    .forEach(deque::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        latch.countDown();
    }

    public static void consumer(BlockingDeque<Path> deque, CountDownLatch latch, Phaser phaser) {
        phaser.register();
        int charactersRead = 0;
        while (true) {
            Path path = deque.poll();

            if (path == null) {
                if (latch.getCount() == 0) {
                    break;
                }

                continue;
            }

            try {
                List<String> lines = Files.readAllLines(path);

                if (lines.get(0).startsWith("SKIP")) {
                    continue;
                }

                if (lines.get(0).startsWith("TERM")) {
                    new Thread(() -> consumer(deque, latch, phaser)).start();
                    break;
                }

                System.out.println(lines);
                charactersRead += lines.stream().mapToInt(String::length).sum();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("I have read " + charactersRead + " characters");
        phaser.arriveAndDeregister();
    }

    public static void main(String[] args) {
        // CountDownLatch is used for telling that the producer is finished. An alternative to poison pill
        final CountDownLatch producersRunning = new CountDownLatch(1);

        // Phaser is used to count the number of runnning consumers
        final Phaser consumersRunning = new Phaser(0);
        final BlockingDeque<Path> deque = new LinkedBlockingDeque<>();


        new Thread(() -> consumer(deque, producersRunning, consumersRunning)).start();
        new Thread(() -> consumer(deque, producersRunning, consumersRunning)).start();

        fileFinder(Paths.get("/Users/mads/data_example"), deque, producersRunning);

        consumersRunning.arriveAndAwaitAdvance();

    }
}
