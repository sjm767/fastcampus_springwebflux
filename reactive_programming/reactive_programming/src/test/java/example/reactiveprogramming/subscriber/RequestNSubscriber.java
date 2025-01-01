package example.reactiveprogramming.subscriber;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestNSubscriber<T> implements Subscriber<T> {
    private final Integer n;
    private Subscription subscription;
    private int count = 0;

    public RequestNSubscriber(Integer n) {
        this.n = n;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(n);
    }

    @Override
    public void onNext(T item) {
        log.info("onNext: {}", item);

        if(count++ % n == 0) {
            log.info("send request");
            this.subscription.request(n);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("onError", throwable);
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
    }
}
