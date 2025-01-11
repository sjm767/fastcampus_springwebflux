package example.nio.selectablechannel;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class NonBlockingSocketChannelTest {

    @Test
    @DisplayName("ServerSocketChannel에서 논블로킹 설정 시 accept에서 블로킹되지 않는다.")
    void testNonBlockingServerSocketChannel() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); // Non-Blocking 설정
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            serverSocketChannel.bind(address);

        SocketChannel clientSocket = serverSocketChannel.accept();
        assertThat(clientSocket).isNull(); // accept에서 블로킹 되지 않기 때문에 null이 반환된다.
    }

    @Test
    @DisplayName("SocketChannel에서 논블로킹 설정 시 connect에서 블로킹 되지 않는다")
    void testNonBlockingSocketChannel() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false); // Non-Blocking 설정
        socketChannel.connect(new InetSocketAddress("localhost", 8080));

        assertThat(socketChannel.isConnected()).isFalse();
    }
}
