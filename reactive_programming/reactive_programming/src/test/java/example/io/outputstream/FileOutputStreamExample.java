package example.io.outputstream;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class FileOutputStreamExample {

    public static void main(String[] args) {
        File file = new File(FileOutputStreamExample.class
            .getClassLoader()
            .getResource("output.txt")
            .getFile());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            String content = "hello world";
            fos.write(content.getBytes());
            fos.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
