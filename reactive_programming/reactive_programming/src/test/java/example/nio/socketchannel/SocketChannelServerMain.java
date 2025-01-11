package example.nio.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketChannelServerMain {

    public static void main(String[] args) {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            serverSocketChannel.bind(address);

            try (SocketChannel clientSocket = serverSocketChannel.accept()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                clientSocket.read(byteBuffer);

                byteBuffer.flip();
                CharBuffer responseResult = StandardCharsets.UTF_8.decode(byteBuffer);
                log.info("request: {}", responseResult);

                String response = "this is server!";
                ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
                clientSocket.write(responseBuffer);
                responseBuffer.flip();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
