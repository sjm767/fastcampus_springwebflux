package example.io.outputstream;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class ByteArrayOutputStreamExample {

    public static void main(String[] args) {
        try (var baos = new ByteArrayOutputStream()) {
            baos.write(100);
            baos.write(101);
            baos.write(102);
            baos.write(103);
            baos.write(104);

            byte[] bytes = baos.toByteArray();
            log.info("bytes: {}", bytes);
        } catch (IOException e) {

        }
    }
}
