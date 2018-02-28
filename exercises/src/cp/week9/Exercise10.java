package cp.week9;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class Exercise10 {
	/*
	- Modify producer_consumer/Sequential from Exercise9 such that such that the producer randomly decides
	  the number of elements that it will puts in the list.
	- Hint: use java.util.Random. Documentation: https://docs.oracle.com/javase/7/docs/api/java/util/Random.html
	- Make it so the number of elements produced by the producer cannot exceed 10000.
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
        int toProduce = ThreadLocalRandom.current().nextInt(10000);

        // int stream range to add water bottles and flower bouquets
        for (int n = 0; n < toProduce; n++) {
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
