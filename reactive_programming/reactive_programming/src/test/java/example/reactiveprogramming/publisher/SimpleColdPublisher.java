package example.reactiveprogramming.publisher;

import example.reactiveprogramming.subscription.SimpleColdSubscription;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

public class SimpleColdPublisher implements Publisher {

    @Override
    public void subscribe(Subscriber subscriber) {
        Iterator<Integer> iterator = Collections.synchronizedList(List.of(1, 2, 3, 4, 5, 6)).iterator();

        SimpleColdSubscription subscription = new SimpleColdSubscription(iterator,
            subscriber);
        subscriber.onSubscribe(subscription);
    }
}
