package example.nio.nonblocking.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

/**
 * 하나의 요청만 처리하고 종료되는 서버
 */
@Slf4j
public class JavaIOSingleServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket()) {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            serverSocket.bind(address);

            Socket clientSocket = serverSocket.accept();
            InputStream in = clientSocket.getInputStream();
            byte[] inBytes = new byte[1024];
            in.read(inBytes);

            log.info("클라이언트로부터 메시지 수신: {}", new String(inBytes).trim());

            OutputStream out = clientSocket.getOutputStream();
            out.write(("This is Server").getBytes());
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
