package example.reactiveprogramming.introduce.projectreactor;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxSimpleMain {

    public static void main(String[] args) throws InterruptedException {
        log.info("start main");
        getItems().subscribe(new SimpleSubscriber<>(3));

        noItem().subscribe(new SimpleSubscriber<>(3));

        log.info("end main");
        Thread.sleep(1000);
    }

    private static Flux<Integer> getItems(){
        return Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    private static Flux<Integer> noItem() {
        return Flux.empty();
    }

}
