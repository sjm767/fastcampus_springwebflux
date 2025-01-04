package example.reactiveprogramming.introduce.projectreactor;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoSimpleExample {
    public static void main(String[] args) {
        log.info("start");
        getItem().subscribe(new SimpleSubscriber<>(3));
        log.info("end");
    }

    private static Mono<Integer> getItem() {
        return Mono.just(1);
    }

    private static Mono<Integer> noItem() {
        return Mono.empty();
    }
}
