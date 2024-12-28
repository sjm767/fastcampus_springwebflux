package example.helper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureHelper {

    /**
     * 새로운 스레드를 생성한 뒤 1을 즉시 반환
     * @return
     */
    public static Future<Integer> getFuture() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            return executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return 1;
                }
            });
        }
        finally {
            executor.shutdown();
        }
    }

    /**
     * 새로운 스레드를 생성한 뒤 1초뒤 1을 반환
     * @return
     */
    public static Future<Integer> getFutureCompleteAfter1s() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            return executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(1000);
                    return 0;
                }
            });
        } finally {
            executor.shutdown();
        }
    }
}
