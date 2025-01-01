package example.reactiveprogramming.publisher;

import example.reactiveprogramming.publisher.FixedIntPublisher.Result;
import example.reactiveprogramming.subscription.IntSubscription;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import lombok.Data;

public class FixedIntPublisher implements Publisher<Result> {

    @Override
    public void subscribe(Subscriber<? super Result> subscriber) {
        var numbers = Collections.synchronizedList(new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7)));

        Iterator<Integer> iterator = numbers.iterator();
        IntSubscription subscription = new IntSubscription(subscriber, iterator);
        subscriber.onSubscribe(subscription);
    }

    @Data
    public static class Result {
        private final Integer value;
        private final Integer requestCount;
    }


}
