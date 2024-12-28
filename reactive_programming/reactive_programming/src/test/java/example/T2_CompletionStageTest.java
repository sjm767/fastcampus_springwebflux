package example;

import example.helper.CompletionStageHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * CompletionStage는 내부적으로 ForkJoinPool을 사용한다.
 * ForkJoinPool은 데몬스레드라서 메인스레드가 종료되면 즉시 종료된다.
 * thenAsync: 상황에 따라 caller가 처리할 수도 있고, callee가 처리할 수도 있음.
 * thenXXXAsync: 무조건 callee가 처리함.
 *
 * 실행상황이 예측되지 않으니 가급적 thenXXXAsync를 사용하자.
 */
@Slf4j
public class T2_CompletionStageTest {


    @Test
    @DisplayName("finishedStage의 thenAccept 테스트. 작업이 즉시끝나서 이 경우는 thenAccept도 메인스레드가 처리한다")
    void finishedStageThenAcceptTest() {
        CompletionStage<Integer> stage = CompletionStageHelper.finishedStage();

        stage.thenAccept((integer) -> {
            log.info("결과값: {}", integer);
        }).thenAccept((integer) -> { //then Accept는 null을 리턴하므로 다음 체이닝에서는 null이 나온다.
            log.info("결과값: {}", integer);
        });

        log.info("메인스레드 작업 끝");
    }

    @Test
    @DisplayName("finishedStage의 thenAcceptAsync 테스트. 작업이 즉시 끝나지만 별도의 스레드가 thenAccept를 처리한다.")
    void finishedStageThenAcceptAsyncTest() {
        CompletionStage<Integer> stage = CompletionStageHelper.finishedStage();

        stage.thenAcceptAsync((integer) -> {
            log.info("결과값: {}", integer);
        }).thenAcceptAsync((integer) -> { //then Accept는 null을 리턴하므로 다음 체이닝에서는 null이 나온다.
            log.info("결과값: {}", integer);
        });

        log.info("메인스레드 작업 끝");
    }

    @Test
    @DisplayName("runningStage의 thenAccept 테스트. 작업이 즉시 끝나지 않아 별도의 스레드가 thenAccept를 처리한다.")
    void runningStageThenAcceptTest() throws InterruptedException {
        CompletionStage<Integer> stage = CompletionStageHelper.runningStage();

        stage.thenAccept((integer) -> {
            log.info("결과값: {}", integer);
        }).thenAccept((integer) -> { //then Accept는 null을 리턴하므로 다음 체이닝에서는 null이 나온다.
            log.info("결과값: {}", integer);
        });

        Thread.sleep(2000); //ForkJoinPool은 데몬 스레드이므로 메인스레드가 끝나는 것을 방지해주어야 한다.
        log.info("메인스레드 작업 끝");
    }

    @Test
    @DisplayName("runningStage의 thenAcceptAsync 테스트. 작업의 종료 유무와 관계없이 무조건 별도의 스레드가 thenAccept를 처리한다")
    void runningStageThenAcceptAsyncTest() throws InterruptedException {
        CompletionStage<Integer> stage = CompletionStageHelper.runningStage();

        stage.thenAcceptAsync((integer) -> {
            log.info("결과값: {}", integer);
        }).thenAcceptAsync((integer) -> { //then Accept는 null을 리턴하므로 다음 체이닝에서는 null이 나온다.
            log.info("결과값: {}", integer);
        });

        Thread.sleep(2000); //ForkJoinPool은 데몬 스레드이므로 메인스레드가 끝나는 것을 방지해주어야 한다.
        log.info("메인스레드 작업 끝");
    }


    @Test
    @DisplayName("thenApplyAsync 테스트")
    void thenApplyAsyncTest() {
        CompletionStage<Integer> stage = CompletionStageHelper.completionStage();

        stage.thenApplyAsync((integer -> integer + 1))
                .thenApplyAsync((integer -> integer + 1))
                .thenAcceptAsync((integer -> {
                    log.info("최종 결과는 {} 입니다", integer);
                }));
    }

    @Test
    @DisplayName("thenComposeAsync 테스트")
    void thenComposeAsyncTest() throws InterruptedException {
        CompletionStage<Integer> stage = CompletionStageHelper.completionStage();

        stage.thenComposeAsync(value -> {
            CompletionStage<Integer> next = CompletionStageHelper.addOne(value);
            log.info("첫번째 thenCompose");
            return next;
        }).thenComposeAsync(value -> {
            CompletionStage<Integer> next = CompletionStageHelper.addOne(value);
            log.info("두번째 thenCompose");
            return next;
        }).thenAcceptAsync(value -> {
            log.info("최종 결과 {}", value);
        });

        Thread.sleep(5000);
        log.info("메인스레드 종료");
    }

    @Test
    @DisplayName("thenRunAsync 테스트")
    void thenRunAsyncTest() throws InterruptedException {
        CompletionStage<Integer> stage = CompletionStageHelper.completionStage();
        stage.thenRunAsync(() -> {
          log.info("thenRun이 실행됨");
        }).thenAcceptAsync(value -> {
            log.info("value: {}", value);
        });

        Thread.sleep(5000);
        log.info("메인스레드 종료");
    }

    @Test
    @DisplayName("exceptionally 테스트")
    void exceptionallyTest() throws InterruptedException {
        CompletionStage<Integer> stage = CompletionStageHelper.completionStage();
        stage.thenApplyAsync(value -> (value / 0))
                .exceptionally(e -> {
                    log.info("{} 예외 발생", e.getMessage()); // 예외 발생 후 정상 흐름으로 돌려짐
                    return 0;
                })
                .thenAcceptAsync(value -> {
                    log.info("정상 흐름으로 돌려받은 값: {}", value);
                });

        Thread.sleep(5000);
        log.info("메인스레드 종료");
    }

    

}
