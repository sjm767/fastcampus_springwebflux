package example.reactiveprogramming;

import example.reactiveprogramming.publisher.FixedIntPublisher;
import example.reactiveprogramming.publisher.FixedIntPublisher.Result;
import example.reactiveprogramming.subscriber.RequestNSubscriber;

public class ReativeStreamMain {

    public static void main(String[] args) throws InterruptedException {
        FixedIntPublisher publisher = new FixedIntPublisher();
        RequestNSubscriber<Result> subscriber = new RequestNSubscriber<>(2);

        publisher.subscribe(subscriber);

        Thread.sleep(1000);
    }
}
