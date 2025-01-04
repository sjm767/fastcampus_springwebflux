package example.io.outputstream;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SocketOutputStream {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket clientSocket = serverSocket.accept();

            byte[] buffer = new byte[1024];
            clientSocket.getInputStream().read(buffer);
            OutputStream outputStream = clientSocket.getOutputStream();

            byte[] bytes = "Hello World".getBytes();
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            bos.write(bytes);
            bos.flush();

            serverSocket.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
