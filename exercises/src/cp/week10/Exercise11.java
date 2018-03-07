package cp.week10;

import java.io.File;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise11
{
	/*
	- Write a method that finds all files with a ".txt" suffix in a given directory.
	- The method must visit the directory recursively, meaning
	  that all .txt files in sub-directories must also be found.
	*/

	public static void findFiles(File directory) {
	    for(File file : directory.listFiles()) {
	        if (file.isFile() && file.getName().endsWith(".txt")) {
                System.out.println(file.getAbsolutePath());
            }

            if (file.isDirectory()) {
	            findFiles(file);
            }
        }
    }

    public static void main(String[] args) {
	    File directory = new File("/Users/mads/data_example");
	    findFiles(directory);
    }
}
