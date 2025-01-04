package example.reactiveprogramming.introduce.projectreactor;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxCompleteExample {
    public static void main(String[] args) {
        log.info("start");
        getItems().subscribe(new SimpleSubscriber<>(3));
        log.info("end");
    }

    private static Flux<Integer> getItems() {
        return Flux.create(sink -> {
            sink.next(1);
            sink.complete();
        });
    }

}
