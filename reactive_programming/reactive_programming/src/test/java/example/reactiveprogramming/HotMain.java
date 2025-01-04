package example.reactiveprogramming;

import example.reactiveprogramming.publisher.SimpleHotPublisher;
import example.reactiveprogramming.subscriber.SimpleHotSubscriber;
import org.junit.jupiter.api.DisplayNameGenerator.Simple;

public class HotMain {

    public static void main(String[] args) throws InterruptedException {
        SimpleHotPublisher hotPublisher = new SimpleHotPublisher();

        // 구독1
        SimpleHotSubscriber subscriber1 = new SimpleHotSubscriber("sub1");
        hotPublisher.subscribe(subscriber1);

        Thread.sleep(1000);

        SimpleHotSubscriber subscriber2 = new SimpleHotSubscriber("sub2");
        hotPublisher.subscribe(subscriber2);

        Thread.sleep(1000);

        subscriber1.cancel();
        subscriber2.cancel();

        Thread.sleep(500);

        SimpleHotSubscriber subscriber3 = new SimpleHotSubscriber("sub3");
        hotPublisher.subscribe(subscriber3);

        Thread.sleep(10000);
        hotPublisher.shutdown();

        subscriber3.cancel();

    }
}
