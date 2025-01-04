package example.io.inputstream;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Objects;

@Slf4j
public class BufferedInputStreamExample {
    public static void main(String[] args) {
        File file = new File(Objects.requireNonNull(FIleInputStreamExample.class
                        .getClassLoader()
                        .getResource("hello.txt"))
                .getFile());

        try (var fis = new FileInputStream(file)) {
            try (var bis = new BufferedInputStream(fis)) {
                var value = 0;
                while ((value = bis.read()) != -1) {
                    log.info("{}", (char) value);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
