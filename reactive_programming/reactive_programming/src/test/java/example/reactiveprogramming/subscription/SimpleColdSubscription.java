package example.reactiveprogramming.subscription;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleColdSubscription implements Subscription {
    private Iterator<Integer> iterator;
    private Subscriber subscriber;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SimpleColdSubscription(Iterator<Integer> iterator, Subscriber subscriber) {
        this.iterator = iterator;
        this.subscriber = subscriber;
    }

    @Override
    public void request(long n) {
        executor.submit(() -> {
            for (int i = 0; i < n; i++) {
                if (iterator.hasNext()) {
                    Integer next = iterator.next();
                    subscriber.onNext(next);
                } else {

                    log.info("onComplete");
                    subscriber.onComplete();
                    executor.shutdown();
                }
            }
        });
    }

    @Override
    public void cancel() {
        subscriber.onComplete();
        executor.shutdown();
        log.info("cancelled");
    }
}
