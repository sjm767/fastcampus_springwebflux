package example.reactiveprogramming.subscriber;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleHotSubscriber implements Subscriber<Integer> {
    private Subscription subscription;
    private final String name;

    public SimpleHotSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        log.info("onSubscribe");
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        log.info("name {} onNext {}", name, item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("onError");
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
    }

    public void cancel() {
        subscription.cancel();
    }
}
