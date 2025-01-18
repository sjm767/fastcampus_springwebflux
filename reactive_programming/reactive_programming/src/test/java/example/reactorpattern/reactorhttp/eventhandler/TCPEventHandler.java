package example.reactorpattern.reactorhttp.eventhandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.SneakyThrows;

public class TCPEventHandler implements EventHandler {
    private static final ExecutorService executor = Executors.newFixedThreadPool(50);
    private final Selector selector;
    private final SocketChannel socketChannel;


    @SneakyThrows
    public TCPEventHandler(Selector selector, SocketChannel socketChannel) {
        this.selector = selector;
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);
        this.socketChannel.register(this.selector, SelectionKey.OP_READ).attach(this);
    }

    @Override
    public void handle() {

    }

    @SneakyThrows
    private static String handleRequest(SocketChannel clientSocketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientSocketChannel.read(buffer);

        if (bytesRead == -1) { // 클라이언트가 연결 종료
            log.info("클라이언트 연결 종료");
            clientSocketChannel.close();
        }

        buffer.flip();
        String resquest = StandardCharsets.UTF_8.decode(buffer).toString();
        log.info("클라이언트로부터 수신: {}", resquest);

        return resquest;
    }

    private static void sendResponse(SocketChannel clientSocketChannel, String responseResult) {
        executor.submit(() -> {
            try {
                Thread.sleep(10);
                clientSocketChannel.write(ByteBuffer.wrap(responseResult.getBytes()));
                clientSocketChannel.close();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
