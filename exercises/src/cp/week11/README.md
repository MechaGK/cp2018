## Exercise 16
Sequential file finder using 
[`Files.walk`](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#walk-java.nio.file.Path-java.nio.file.FileVisitOption...-)

## Exercise 17
Consumer threads using 
[`BlockingDeque.Poll(timout, unit)`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingDeque.html#poll-long-java.util.concurrent.TimeUnit-) 
with timeout. 
Consumers shutdown if they don't get a Path within a specific time frame

## Exercise 18
Executor service to handle consumers and poison pill to shutdown consumers.

## Exercise 19
Consumers are threads. Threads are shutdown when a 
[`CountDownLatch`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CountDownLatch.html)
is at zero.
Number of threads is kept track of with a 
[`Phaser`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Phaser.html)