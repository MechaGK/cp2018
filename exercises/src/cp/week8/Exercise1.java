package cp.week8;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise1
{
	/*
	- Create a Counter class storing an integer (a field called i), with an increment and decrement method.
	- Make Counter Thread-safe (see thread safety in the book chapter readings)
	- Does it make a different to declare i private or public?
	*/

	public static class Counter {
	    private volatile int i = 0;

	    public synchronized void increment() {
	        i++;
        }

        public synchronized void decrement() {
	        i--;
        }

        public synchronized int getValue() {
	        return i;
        }
    }

	public static void main(String[] args) {
	    Counter counter = new Counter();
    }
}
