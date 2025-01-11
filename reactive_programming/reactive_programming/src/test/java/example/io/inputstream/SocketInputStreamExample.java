package example.io.inputstream;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketInputStreamExample {

    public static void main(String[] args) {
        try {
            // 서버 소켓 생성 & Bind
            ServerSocket serverSocket = new ServerSocket(8080);

            // 클라이언트 접속 대기
            Socket clientSocket = serverSocket.accept();

            // InputStream 획득
            InputStream inputStream = clientSocket.getInputStream();

            try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
                byte[] buffer = new byte[1024];
                int bytesRead = bis.read(buffer);
                String inputLine = new String(buffer, 0, bytesRead);
                log.info("bytes: {}", inputLine);
            }

            clientSocket.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
