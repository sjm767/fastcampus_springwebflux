package example.asyncprogramming;


import example.asyncprogramming.helper.FutureHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.*;


public class T1_FutureTest {


    @Test
    @DisplayName("isDone 메서드를 테스트 한다.")
    void isDoneTest() throws ExecutionException, InterruptedException {
        Future<Integer> future = FutureHelper.getFuture();

        assertThat(future.isDone()).isFalse();
        assertThat(future.isCancelled()).isFalse();

        Integer result = future.get();
        assertThat(result).isEqualTo(1);
        assertThat(future.isDone()).isTrue();
        assertThat(future.isCancelled()).isFalse();
    }

    @Test
    @DisplayName("cancel 메서드를 테스트 한다")
    void cancelTest() {
        Future<Integer> future = FutureHelper.getFuture();
        boolean cancel1 = future.cancel(true);

        assertThat(cancel1).isTrue();
        assertThat(future.isDone()).isTrue();
        assertThat(future.isCancelled()).isTrue();

        boolean cancel2 = future.cancel(true);
        assertThat(cancel2).isFalse(); // 이미 취소된 것을 다시 취소하면 false가 리턴된다.
        assertThat(future.isDone()).isTrue();
        assertThat(future.isCancelled()).isTrue();
    }
}


