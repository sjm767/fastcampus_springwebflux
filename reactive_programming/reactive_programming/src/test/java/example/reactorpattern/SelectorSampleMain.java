package example.reactorpattern;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelectorSampleMain {

    public static void main(String[] args) {
        try (Selector selector = Selector.open(); ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 이벤트가 준비될 떄 까지 blocking
                selector.select();

                // 준비 완료된 이벤트를 처리
                selector.selectedKeys().forEach(key -> {
                    // 준비 완료된 키는 제거하고 작업을 수행한다.
                    selector.selectedKeys().remove(key);

                    if (key.isAcceptable()) {
                        try {
                            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (key.isReadable()) {
                        try {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            socketChannel.read(buffer);
                            buffer.flip();

                            CharBuffer request = StandardCharsets.UTF_8.decode(buffer);
                            log.info("request: {}", request);

                            socketChannel.write(
                                ByteBuffer.wrap(("This is Server using selector").getBytes())
                            );
                            buffer.clear();
                            socketChannel.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
