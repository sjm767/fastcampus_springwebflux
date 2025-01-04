package example.reactiveprogramming.introduce.projectreactor;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FluxToMonoExample {

    public static void main(String[] args) throws InterruptedException {
        log.info("start main");

        Mono.from(getItems()).subscribe(new SimpleSubscriber<>(3));

        // Flux의 목록 자체를 Mono로 전달
        Mono.from(getItems().collectList())
            .subscribe(new SimpleSubscriber<>(3)
            );

        log.info("end main");
        Thread.sleep(1000);
    }

    private static Flux<Integer> getItems(){
        return Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

}
