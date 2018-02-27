package cp.week8;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise5 {
	/*
	- Apply the technique for fixing Listing 4.14 to Listing 4.15 in the book, but to the following:
	- Create a thread-safe Counter class that stores an int and supports increment and decrement.
	- Create a new thread-safe class Point, which stores two Counter objects.
	- The two counter objects should be public.
	- Implement the method boolean areEqual() in Point, which returns true if the two counters store the same value.
	- Question: Is the code you obtained robust with respect to client-side locking (see book)?
				Would it help if the counters were private?
	*/

    public static class Counter {
        private volatile int i = 0;
        private Object lock;

        public Counter(int i, Object lock) {
            this.i = i;
            this.lock = lock;
        }

        public void increment() {
            synchronized (lock) {
                i++;
            }
        }

        public void decrement() {
            synchronized (lock) {
                i--;
            }
        }

        public int getValue() {
            synchronized (lock) {
                return i;
            }
        }
    }

    public class Point {
        private Object lock = new Object();
        public Counter x = new Counter(0, lock);
        public Counter y = new Counter(0, lock);

        public boolean areEqual() {
            synchronized (lock) {
                return x.getValue() == y.getValue();
            }
        }
    }
}
