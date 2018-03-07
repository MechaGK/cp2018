package cp.week10;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise13
{
	/*
	- Modify Exercise12 to use an Executor instead of
	  manually-controlled threads.
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

        phaser.register();
        findFiles(directory);

        phaser.awaitAdvance(0);
        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }
}
