package example.reactiveprogramming.introduce.projectreactor;


import java.util.List;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoToFlux {
    public static void main(String[] args) {
        log.info("start");
        getItems().flux().subscribe(new SimpleSubscriber<>(3));

        // Mono의 배열에 담긴 element를 Flux에 하나씩 전달
        getItems().flatMapMany(Flux::fromIterable)
                .subscribe(new SimpleSubscriber<>(3));

        log.info("end");
    }

    private static Mono<List<Integer>> getItems() {
        return Mono.just(List.of(1, 2, 3, 4, 5));
    }
}
