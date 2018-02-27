package cp.week8;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise4
{
	/*
	- Write the example from Listing 4.2 in the book.
	- Add a method that returns a reference to the internal field mySet.
	- Use the new method from concurrent threads to create unsafe access to mySet.
	*/

	public static class PersonSet {
	    private final Set<Person> mySet = new HashSet<Person>();

	    public synchronized void addPerson(Person p) {
	        mySet.add(p);
        }

        public synchronized boolean containsPerson(Person p) {
	        return mySet.contains(p);
        }

        public Set<Person> getSet() {
	        return mySet;
        }
    }

    private static class Person {
	    private final String name;

	    Person(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
	    PersonSet set = new PersonSet();

	    Thread safeThread = new Thread(() -> {
	        set.addPerson(new Person("Peter"));
	        set.addPerson(new Person("Kasper"));
	        Person p = new Person("Jens");
	        set.addPerson(p);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(set.containsPerson(p));
        });

	    Thread unsafeThread = new Thread(() -> {
	        Set<Person> unsafeSet = set.getSet();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            unsafeSet.clear();
        });

	    safeThread.start();
	    unsafeThread.start();
    }
}
