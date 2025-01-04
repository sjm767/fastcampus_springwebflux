package example.reactiveprogramming.introduce.projectreactor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class SimpleSubscriber<T> implements Subscriber<T> {
    private final Integer count;
    private Subscription subscription;


    public SimpleSubscriber(Integer count) {
        this.count = count;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        log.info("onSubscribe");
        this.subscription = subscription;
        subscription.request(count);
    }

    @SneakyThrows
    @Override
    public void onNext(T t) {
        log.info("onNext: {}", t);
        Thread.sleep(1000);
        subscription.request(count);
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("onError");
        log.error(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
    }
}
