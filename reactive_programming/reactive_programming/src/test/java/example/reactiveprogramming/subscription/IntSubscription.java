package example.reactiveprogramming.subscription;

import example.reactiveprogramming.publisher.FixedIntPublisher.Result;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class IntSubscription implements Flow.Subscription {

    private final Flow.Subscriber<? super Result> subscriber;
    private final Iterator<Integer> numbers;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicInteger count = new AtomicInteger(1);
    private final AtomicBoolean isCompleted = new AtomicBoolean(false);

    public IntSubscription(Subscriber<? super Result> subscriber, Iterator<Integer> numbers) {
        this.subscriber = subscriber;
        this.numbers = numbers;
    }

    @Override
    public void request(long n) {
        executor.submit(() -> {
            for (int i = 0; i < n; i++) {
                if (numbers.hasNext()) {
                    Integer number = numbers.next();
                    numbers.remove();
                    subscriber.onNext(new Result(number, count.get()));
                } else {
                    // expect: 현재 값으로 예상하는 값. 예상과 동일할 경우 업데이트할 값.
                    boolean isChanged = this.isCompleted.compareAndSet(false, true);
                    if (isChanged) {
                        executor.shutdown();
                        subscriber.onComplete();
                        isCompleted.set(true);
                    }
                    break;
                }
            }
        });
    }

    @Override
    public void cancel() {
        subscriber.onComplete();
    }
}
