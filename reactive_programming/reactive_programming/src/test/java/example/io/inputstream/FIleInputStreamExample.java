package example.io.inputstream;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class FIleInputStreamExample {
    public static void main(String[] args) {
        File file = new File(Objects.requireNonNull(FIleInputStreamExample.class
                        .getClassLoader()
                        .getResource("hello.txt"))
                .getFile());

        try (var fis = new FileInputStream(file)) {
            var value = 0;

            while ((value = fis.read()) != -1) {
                log.info("value: {}", (char) value);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
