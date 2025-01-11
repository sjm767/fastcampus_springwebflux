package example.nio.nonblocking.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaNIOClient {

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            boolean connected = socketChannel.connect(address);

            log.info("Connected to {}", address);

            // 서버로 request 전송
            socketChannel.write(
                ByteBuffer.wrap(("This is Client").getBytes(StandardCharsets.UTF_8)));


            // 서버로부터 response 수신
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            socketChannel.read(buffer);
            CharBuffer responseResult = StandardCharsets.UTF_8.decode(buffer);
            log.info("서버로부터 메시지 수신: {}", responseResult);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
