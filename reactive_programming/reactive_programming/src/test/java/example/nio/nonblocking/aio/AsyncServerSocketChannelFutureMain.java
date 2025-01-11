package example.nio.nonblocking.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncServerSocketChannelFutureMain {
    public static void main(String[] args) {

        try (AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            serverSocketChannel.bind(address);

            Future<AsynchronousSocketChannel> future = serverSocketChannel.accept();

            while (!future.isDone()) {
                log.info("waiting connection...");
            }

            log.info("accept connection");
            AsynchronousSocketChannel clientSocket = future.get();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            clientSocket.read(buffer);

            log.info("read completed");
            buffer.flip();
            CharBuffer request = StandardCharsets.UTF_8.decode(buffer);
            log.info("request: {}", request);

            clientSocket.write(ByteBuffer.wrap(("This is AsyncServerSocketChannel Server").getBytes()));
            clientSocket.close();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
