package example.io.readerwriter;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileWriterExample {
    public static void main(String[] args) {
        File file = new File(FileWriterExample.class
                .getClassLoader()
                .getResource("koreanhello.txt")
                .getFile());

        var charset = StandardCharsets.UTF_8;
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("안녕");
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
