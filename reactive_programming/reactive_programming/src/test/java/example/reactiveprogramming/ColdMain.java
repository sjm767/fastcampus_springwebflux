package example.reactiveprogramming;

import example.reactiveprogramming.publisher.SimpleColdPublisher;
import example.reactiveprogramming.subscriber.SimpleColdSubscriber;

public class ColdMain {
    public static void main(String[] args) throws InterruptedException {
        SimpleColdPublisher coldPublisher = new SimpleColdPublisher();
        SimpleColdSubscriber publisher1 = new SimpleColdSubscriber("publisher1");

        coldPublisher.subscribe(publisher1);

        Thread.sleep(5000);
        SimpleColdSubscriber publisher2 = new SimpleColdSubscriber("publisher2");

        coldPublisher.subscribe(publisher2);
    }
}
