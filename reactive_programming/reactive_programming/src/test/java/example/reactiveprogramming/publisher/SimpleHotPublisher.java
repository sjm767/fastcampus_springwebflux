package example.reactiveprogramming.publisher;

import example.reactiveprogramming.subscriber.SimpleHotSubscriber;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleHotPublisher implements Publisher {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final List<Integer> numbers = new ArrayList<>();
    private final AtomicInteger count = new AtomicInteger(0);
    private Future<?> task;
    private List<SimpleHotSubscription> subscriptions = new ArrayList<>();

    public SimpleHotPublisher() {
        task = executor.submit(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                numbers.add(count.getAndIncrement());
//                log.info(numbers.toString());
                subscriptions.forEach(SimpleHotSubscription::wakeUp);
                Thread.sleep(100);
            }
            return null;
        });
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        SimpleHotSubscription subscription = new SimpleHotSubscription(subscriber);
        subscriptions.add(subscription);
        subscriber.onSubscribe(subscription);
    }

    public void shutdown() {
        task.cancel(true);
        executor.shutdown();
    }

    class SimpleHotSubscription implements Subscription {
        private int offset;
        private long requiredOffset;
        private final Subscriber<? super Integer> subscriber;
        private final ExecutorService executor = Executors.newSingleThreadExecutor();

        public SimpleHotSubscription(Subscriber<? super Integer> subscriber) {
            int lastOffset = numbers.size();
            this.offset = lastOffset;
            this.requiredOffset = lastOffset;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            requiredOffset += n;
            onNextIfPossible();
        }

        public void wakeUp() {
            onNextIfPossible();
        }

        private void onNextIfPossible() {
            executor.submit(() -> {
                while(offset < requiredOffset && offset < numbers.size()) {
                    Integer value = numbers.get(offset++);
                    subscriber.onNext(value);
                }
            });
        }

        @Override
        public void cancel() {
            subscriptions.remove(this);
            executor.shutdown();
        }
    }
}
