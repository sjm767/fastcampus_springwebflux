package example.nio.nonblocking.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * Java NIO로 처리하는 블로킹 방식 서버
 */
@Slf4j
public class JavaNIOBlockingServer {

    public static void main(String[] args) {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            serverSocketChannel.bind(address);

            while (true) {
                SocketChannel clientSocketChannel = serverSocketChannel.accept();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                clientSocketChannel.read(buffer);

                buffer.flip();
                CharBuffer responseResult = StandardCharsets.UTF_8.decode(buffer);
                log.info("클라이언트로부터 수신: {}", responseResult);

                clientSocketChannel.write(ByteBuffer.wrap(("This is Server").getBytes()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
