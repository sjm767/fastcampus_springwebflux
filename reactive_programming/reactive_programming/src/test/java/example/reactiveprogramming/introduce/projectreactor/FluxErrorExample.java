package example.reactiveprogramming.introduce.projectreactor;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxErrorExample {

    public static void main(String[] args) {
        log.info("start");
        Flux<Integer> items = getItems();
        items.subscribe(new SimpleSubscriber<>(3));
        log.info("end");
    }

    private static Flux<Integer> getItems() {
        return Flux.create(fluxSink -> {
            fluxSink.next(0);
            fluxSink.next(1);
            fluxSink.error(new RuntimeException("error in flux"));
            fluxSink.complete();
        });
    }





}
