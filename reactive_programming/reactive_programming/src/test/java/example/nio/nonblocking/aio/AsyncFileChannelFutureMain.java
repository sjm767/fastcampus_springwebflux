package example.nio.nonblocking.aio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncFileChannelFutureMain {

    public static void main(String[] args) {
        File file = new File(Objects.requireNonNull(AsyncFileChannelFutureMain.class
                .getClassLoader()
                .getResource("aio-file-channel.txt"))
            .getFile());

        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file.toPath())){
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            Future<Integer> future = fileChannel.read(buffer, 0);

            while (!future.isDone()){
                log.info("reading...");
            }

            log.info("read completed");
            buffer.flip();

            CharBuffer readResult = StandardCharsets.UTF_8.decode(buffer);
            log.info("read result: {}", readResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
