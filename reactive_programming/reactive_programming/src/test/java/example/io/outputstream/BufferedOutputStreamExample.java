package example.io.outputstream;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class BufferedOutputStreamExample {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(BufferedOutputStreamExample.class
            .getClassLoader()
            .getResource("output.txt")
            .getFile());

        FileOutputStream fos = new FileOutputStream(file);
        try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            String content = "Hello world in bufferd";

            bos.write(content.getBytes());
            bos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
