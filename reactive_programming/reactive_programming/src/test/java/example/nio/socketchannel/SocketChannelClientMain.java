package example.nio.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketChannelClientMain {

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            boolean connected = socketChannel.connect(address);
            log.info("connected: {}", connected);

            // Request
            String request = "This is Client";
            ByteBuffer requestBuffer = ByteBuffer.wrap(request.getBytes());
            socketChannel.write(requestBuffer);
            requestBuffer.clear();

            // Response
            ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
            while (socketChannel.read(responseBuffer) > 0) {
                responseBuffer.flip();
                CharBuffer responseResult = StandardCharsets.UTF_8.decode(responseBuffer);
                log.info("response: {}", responseResult);
                responseBuffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
