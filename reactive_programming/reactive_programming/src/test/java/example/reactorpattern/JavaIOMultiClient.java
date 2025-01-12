package example.reactorpattern;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaIOMultiClient {

    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    public static void main(String[] args) {
        List<CompletableFuture> futures = new ArrayList<>();

        log.info("start main");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try (Socket socket = new Socket()) {
                    InetSocketAddress address = new InetSocketAddress("localhost", 8080);
                    socket.connect(address);

                    OutputStream out = socket.getOutputStream();
                    String msg = "This is client";

                    out.write(msg.getBytes());
                    out.flush();

                    InputStream in = socket.getInputStream();
                    byte[] responseBytes = new byte[1024];
                    in.read(responseBytes);
                    log.info("서버로부터 메시지 수신: {}", new String(responseBytes).trim());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, executorService);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        log.info("end main");
        long end = System.currentTimeMillis();
        log.info("elasped: {} ms", end - start);

        executorService.shutdown();
    }

}
