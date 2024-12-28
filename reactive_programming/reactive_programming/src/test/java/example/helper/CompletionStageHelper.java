package example.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
public class CompletionStageHelper {

    /**
     * 결과 1을 가진 완료된 CompletionStage를 반환
     * @return
     */
    public static CompletionStage<Integer> finishedStage() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return 1;
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return future;
    }

    /**
     * 1초를 sleep 한 후 1을 반환하는 CompletableStage를 반환
     * @return
     */
    public static CompletionStage<Integer> runningStage() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                log.info("아직 실행 중");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 1;
        });
        return future;
    }

    /**
     * 결과 값을 즉시 반환함.
     * @return
     */
    public static CompletionStage<Integer> completionStage() {
        return CompletableFuture.completedStage(1);
    }

    /**
     * 값을 전달받아 1을 더한 작업을 하는 CompletionStage를 반환
     * @param value
     * @return
     */
    public static CompletionStage<Integer> addOne(int value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return value + 1;
        });
    }
}
