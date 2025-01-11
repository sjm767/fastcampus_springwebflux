package example.nio.nonblocking.aio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncFileChannelCallbackMain {

    public static void main(String[] args) {
        File file = new File(Objects.requireNonNull(AsyncFileChannelCallbackMain.class
                .getClassLoader()
                .getResource("aio-file-channel.txt"))
            .getFile());

        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file.toPath())){
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            fileChannel.read(buffer, 0, null, new CompletionHandler<Integer, Object>() {
                @SneakyThrows
                @Override
                public void completed(Integer result, Object attachment) {
                    log.info("read completed");
                    buffer.flip();

                    CharBuffer readResult = StandardCharsets.UTF_8.decode(buffer);
                    log.info("read result: {}", readResult);
                    fileChannel.close();
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    log.info("read failed");
                }
            });

            while (fileChannel.isOpen()) {
                log.info("Reading...");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
