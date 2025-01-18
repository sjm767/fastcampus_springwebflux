package example.nio.nonblocking.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Java NIO로 처리하는 논블로킹 방식 서버
 */
@Slf4j
public class JavaNIONonBlockingServer {

    public static void main(String[] args) {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false); // 논블로킹 설정

            while (true) {
                SocketChannel clientSocketChannel = serverSocketChannel.accept();
                if (clientSocketChannel == null) {
                    log.info("wait accept");
                    Thread.sleep(100);
                    continue;
                }

                handleRequest(clientSocketChannel);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static void handleRequest(SocketChannel clientSocketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (clientSocketChannel.read(buffer) == 0) {
            log.info("Reading...");
        }

//        Thread.sleep(10);

        buffer.flip();
        CharBuffer responseResult = StandardCharsets.UTF_8.decode(buffer);
        log.info("클라이언트로부터 수신: {}", responseResult);

        clientSocketChannel.write(ByteBuffer.wrap(("This is Server").getBytes()));
    }

}
