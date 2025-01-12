package example.nio.nonblocking.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncServerSocketChannelCallbackMain {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1); // 서버 종료 방지용

        try (AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            serverSocketChannel.bind(address);

            serverSocketChannel.accept(null,
                new CompletionHandler<>() {
                    @Override
                    public void completed(AsynchronousSocketChannel clientSocket,
                        Object attachment) {
                        log.info("client connected");
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        clientSocket.read(buffer, null,
                            new CompletionHandler<Integer, ByteBuffer>() {
                                @SneakyThrows
                                @Override
                                public void completed(Integer result, ByteBuffer attachment) {
                                    log.info("client read completed");
                                    buffer.flip();

                                    CharBuffer request = StandardCharsets.UTF_8.decode(buffer);
                                    log.info("request: {}", request);

                                    clientSocket.write(
                                        ByteBuffer.wrap(
                                            ("This is response").getBytes(StandardCharsets.UTF_8))
                                    );
                                    clientSocket.close();
                                    latch.countDown();
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    log.info("error");
                                }
                            });

                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        log.info("serverSocket channel accept failed");
                    }
                });

            latch.await();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
