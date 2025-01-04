package example.io.readerwriter;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileReaderExample {
    public static void main(String[] args) {
        File file = new File(FileReaderExample.class
                .getClassLoader()
                .getResource("koreanhello.txt")
                .getFile());

        var charset = StandardCharsets.UTF_8;
        try (FileReader fileReader = new FileReader(file)) {
            var value = 0;

            while ((value = fileReader.read()) != -1) {
                log.info("value: {}", (char) value);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
