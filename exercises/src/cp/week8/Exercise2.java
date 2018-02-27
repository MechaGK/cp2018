package cp.week8;

import java.util.ArrayList;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise2 {
	/*
	- Create a static class for cacheing the names of some cities in a static field.
	- Initialise the static field with some cities, e.g., Copenhagen and Odense.
	- Start two threads that each adds some (different) cities.
	- The two threads will share the static field, potentially having problems.
	- Make the static field for city names a ThreadLocal to make it local to threads.
	*/

    public static class CityCache {
        public static ThreadLocal<ArrayList<String>> cityList = new ThreadLocal<>();
    }

    public static void main(String[] args) {
        CityCache.cityList.set(new ArrayList<>());
        CityCache.cityList.get().add("Copenhagen");
        CityCache.cityList.get().add("Odense");

        Thread t1 = new Thread(() -> {
            CityCache.cityList.set(new ArrayList<>());
            CityCache.cityList.get().add("Aalborg");
            CityCache.cityList.get().add("Aarhus");
            System.out.println(CityCache.cityList.get());
        });

        Thread t2 = new Thread(() -> {
            CityCache.cityList.set(new ArrayList<>());
            CityCache.cityList.get().add("Berlin");
            CityCache.cityList.get().add("Amsterdam");
            System.out.println(CityCache.cityList.get());
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(CityCache.cityList.get());
    }
}
