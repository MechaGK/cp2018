package cp.week10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise15
{
	/*
	- Rewrite the directory visitor from the previous exercises
	  using stream(s).
	- Compare running time of:
	  * Sequential stream.
	  * Parallel stream.
	- How does this compare to the running times of Exercise 14?
	*/

	public static void findFiles(Path path) {
        try {
            Files.list(path).parallel().forEach(subpath -> {
                if (!Files.isDirectory(subpath)) {
                    if (subpath.getFileName().toString().endsWith(".txt")) {
                        System.out.println(subpath.getFileName());
                    }
                } else {
                    findFiles(subpath);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
	    Path path = Paths.get("/Users/mads/data_example");
	    long start = System.nanoTime();
	    findFiles(path);
        System.out.println((System.nanoTime() - start) / 1000_000_000.);
    }
}
