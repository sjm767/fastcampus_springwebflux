package example.nio.nonblocking.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaIOClient {

    public static void main(String[] args) {
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

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
