package example.asyncprogramming;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class T0_SyncAsyncBlockingNonBlockingTest {

    static class A {

        static int getResult(Integer num) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return num + 1;
        }
    }

    static class B {
        static int getResult(Integer num, Function<Integer, Integer> fc) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return fc.apply(num);
        }
    }

    @Test
//    @DisplayName("동기 + 블로킹")
    void testSyncBlocking() {
        int result = A.getResult(1);
        log.info("getResult: {}", result);
    }

    @Test
//    @DisplayName("동기 + 블로킹이지만 작업처리를 callee에서 수행한다. (함수 관점에서의 비동기)")
    void testSyncBlocking2() {
        int result = B.getResult(1, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer + 1;
            }
        });
        log.info("getResult: {}", result);
    }

    @Test
    @DisplayName("동기 + 논블로킹")
    void testSyncNonBlocking() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("작업을 시작함");
                Thread.sleep(5000);
                log.info("작업이 완료됨");
                return 0;
            }
        });

        int counter = 0;
        while(!future.isDone()) {
            counter++;
            log.info("작업이 완료되는 동안 다른 작업 중.., {}", counter);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("작업이 완료되었음 {}", counter);
    }

    @Test
    @DisplayName("비동기 + 블로킹")
    void testAsyncBlocking() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Integer> future = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(5000);
                    log.info("비동기 작업을 완료함");
                    return 0;
                }
            });

            Integer result = future.get();
            log.info("getResult: {}", result);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            executor.shutdown();
        }
        log.info("메인 작업을 완료함");
    }

    @Test
    @DisplayName("비동기 + 논블로킹")
    void testAsyncNonBlocking() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                log.info("비동기 작업을 시작함");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("비동기 작업을 완료함");
            }
        });

        log.info("메인 작업을 완료함");
        executor.shutdown();

        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("비동기 작업을 완료함");

    }
}
