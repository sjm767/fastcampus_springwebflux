package example.reactiveprogramming.subscriber;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleColdSubscriber implements Subscriber<Integer> {
    private Subscription subscription;
    private String name;

    public SimpleColdSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
        log.info("onSubscribe {}", name);
    }

    @Override
    public void onNext(Integer item) {
        log.info("onNext {}, {}", name, item);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
