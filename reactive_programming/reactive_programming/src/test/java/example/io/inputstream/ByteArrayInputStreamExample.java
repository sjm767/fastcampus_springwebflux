package example.io.inputstream;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
public class ByteArrayInputStreamExample {

    public static void main(String[] args) {
        byte[] bytes = {100, 101, 102, 103, 104};

        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            var value = 0;
            while ((value = bais.read()) != -1) {
                log.info("value: {}", value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 바이트 전체 읽기
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            byte[] allBytes = bais.readAllBytes();
            log.info("allBytes: {}", allBytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
