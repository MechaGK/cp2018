package cp.week9;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise9 {
    /*
    - Make your implementation of producer_consumer/Sequential from Exercise8 thread-safe using synchronized blocks.
    - Question: Does your implementation guarantee that all produced items in the list are also consumed? Why?
    */

    private static class Product {
        private final String name;
        private final String attributes;

        public Product(String name, String attributes) {
            this.name = name;
            this.attributes = attributes;
        }

        public String toString() {
            return name + " : " + attributes;
        }
    }

    private static final Deque<Product> THE_LIST = new ConcurrentLinkedDeque<>();
    private static final CountDownLatch LATCH = new CountDownLatch(1);

    private static void produce(Deque<Product> list) {
        // int stream range to add water bottles and flower bouquets
        for (int n = 0; n < 10000; n++) {
            list.add(new Product("Water bottle " + n, "Fresh"));
            list.add(new Product("Flower bouquet " + n, "Roses"));
        }

        LATCH.countDown();
    }

    private static void consume(Deque<Product> list) {
        while (true) {
            Product product = list.poll();

            if (product != null) {
                System.out.println(product);
            } else if (LATCH.getCount() == 0){
                break;
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> produce(THE_LIST));
        Thread t2 = new Thread(() -> consume(THE_LIST));
        Thread t3 = new Thread(() -> consume(THE_LIST));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
