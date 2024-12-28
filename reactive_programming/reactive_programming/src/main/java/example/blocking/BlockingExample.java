package example.blocking;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BlockingExample {

    public static void main(String[] args) {
        Consumer<Integer> consumer = getConsumer();
        consumer.accept(1);

    }

    private static Consumer<Integer> getConsumer() {
        return integer -> log.info("integer is {}", integer);
    }

}
