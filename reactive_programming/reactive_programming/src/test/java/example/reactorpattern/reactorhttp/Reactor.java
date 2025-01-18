package example.reactorpattern.reactorhttp;

import example.reactorpattern.reactorhttp.eventhandler.Acceptor;
import example.reactorpattern.reactorhttp.eventhandler.EventHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Java NIO로 처리하는 논블로킹 방식 서버
 */
@Slf4j
public class Reactor implements Runnable {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final EventHandler acceptor;

    @SneakyThrows
    public Reactor(int port) {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        acceptor = new Acceptor(selector, serverSocketChannel);
        serverSocketChannel.bind(new InetSocketAddress("localhost", port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT).attach(acceptor);
    }

    @Override
    public void run() {
        executor.submit(() -> {
            try {
                selector.select();

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    selector.selectedKeys().remove(key);

                    dispatch(key);
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void dispatch(SelectionKey key) {
        EventHandler eventHandler = (EventHandler) key.attachment();

        if (key.isAcceptable() || key.isReadable()) {
            eventHandler.handle();
        }
    }
//
//    public static void main(String[] args) {
//
//        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//            Selector selector = Selector.open()) {
//
//            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
//            serverSocketChannel.bind(address);
//            serverSocketChannel.configureBlocking(false); // 논블로킹 설정
//
//            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//
//            while (true) {
//                log.info("waiting for client");
//                selector.select(); // blocking
//
//                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//                while (iterator.hasNext()) {
//                    SelectionKey key = iterator.next();
//                    iterator.remove(); // 키를 안전하게 제거
//                    if (!key.isValid()) {
//                        continue;
//                    }
//
//                    if (key.isAcceptable()) {
//                        try {
//                            SocketChannel clientSocket = ((ServerSocketChannel) key.channel()).accept();
//                            clientSocket.configureBlocking(false);
//                            clientSocket.register(selector, SelectionKey.OP_READ);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    } else if (key.isReadable()) {
//                        SocketChannel socketChannel = (SocketChannel) key.channel();
//                        try {
//                            String responseResult = handleRequest(socketChannel);
//                            sendResponse(socketChannel, responseResult);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//
//    @SneakyThrows
//    private static String handleRequest(SocketChannel clientSocketChannel) throws IOException {
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        int bytesRead = clientSocketChannel.read(buffer);
//
//        if (bytesRead == -1) { // 클라이언트가 연결 종료
//            log.info("클라이언트 연결 종료");
//            clientSocketChannel.close();
//        }
//
//        buffer.flip();
//        String resquest = StandardCharsets.UTF_8.decode(buffer).toString();
//        log.info("클라이언트로부터 수신: {}", resquest);
//
//        return resquest;
//    }
//
//    private static void sendResponse(SocketChannel clientSocketChannel, String responseResult) {
//        executor.submit(() -> {
//            try {
//                Thread.sleep(10);
//                clientSocketChannel.write(ByteBuffer.wrap(responseResult.getBytes()));
//                clientSocketChannel.close();
//            } catch (IOException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

}