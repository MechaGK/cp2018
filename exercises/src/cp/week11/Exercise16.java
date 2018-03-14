package cp.week11;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise16 {
	/*
	- Rewrite the "file finder" from week 10 such that whenever a file with
	".txt" suffix is found, a Path object representing the file is put on a
	shared BlockingDeque deque. (You can use LinkedBlockingDeque as
	implementation.)
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

    public static void main(String[] args) {
        BlockingDeque<Path> deque = new LinkedBlockingDeque<>();

        fileFinder(Paths.get("/Users/mads/data_example"), deque);

        System.out.println(deque);
    }
}
