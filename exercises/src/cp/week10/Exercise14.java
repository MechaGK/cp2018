package cp.week10;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise14
{
	/*
	- Experiment using different kinds of executors, see:
	  https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executors.html
	- Compare running time of at least:
	  * Single threaded executor.
	  * Fixed thread pool with a number of threads equal to number of CPU cores.
	  * Cached thread pool.
	  * Work stealing pool.
	*/

    public static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    public static final Phaser phaser = new Phaser();

    public static void findFiles(File directory) {
        for(File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                System.out.println(file.getAbsolutePath());
            }

            if (file.isDirectory()) {
                phaser.register();
                executorService.submit(() -> findFiles(file));
            }
        }

        phaser.arriveAndDeregister();
    }

    public static void main(String[] args) {
        File directory = new File("/Users/mads/data_example");

        long start = System.nanoTime();
        phaser.register();
        findFiles(directory);

        phaser.awaitAdvance(0);
        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();

        System.out.println((end - start) / 1000_000_000.);

        System.out.println("done");
    }
}
