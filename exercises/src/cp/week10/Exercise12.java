package cp.week10;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise12
{
	/*
	- Modify Exercise11 such that each sub-directory is visited
	  by a dedicated thread.
	*/

    public static void findFiles(File directory) {
        ArrayList<Thread> threads = new ArrayList<>();

        for(File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                System.out.println(file.getAbsolutePath());
            }

            if (file.isDirectory()) {
                Thread thread = new Thread(() -> {
                    findFiles(file);
                });

                threads.add(thread);

                thread.start();
            }
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        File directory = new File("/Users/mads/data_example");
        findFiles(directory);
        System.out.println("done");
    }
}
