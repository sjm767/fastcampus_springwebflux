package example.nio.buffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BufferMain {

    public static void main(String[] args) {
        File file = new File(Objects.requireNonNull(BufferMain.class
                .getClassLoader()
                .getResource("file-channel.txt"))
            .getFile());

        try (FileChannel fileChannel = FileChannel.open(file.toPath())) {
            // 초기화
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            logPosition("버퍼 생성 후", byteBuffer);

            // 파일 읽기
            fileChannel.read(byteBuffer);
            logPosition("파일 읽기 후", byteBuffer);

            // flip으로 읽기 모드 전환
            byteBuffer.flip();
            logPosition("flip으로 읽기 모드 전환", byteBuffer);

            // 읽기 모드 전환 후 limit 까지 읽음.
            CharBuffer result = StandardCharsets.UTF_8.decode(byteBuffer);
            log.info("result: {}", result);
            logPosition("읽기 모드 전환 후 limit 까지 읽음", byteBuffer);

            // rewind 통해 다시 읽기 모드로 전환
            byteBuffer.rewind();
            logPosition("rewind 통해 다시 읽기", byteBuffer);

            // 다시 읽기 결과 출력
            CharBuffer result2 = StandardCharsets.UTF_8.decode(byteBuffer);
            log.info("result: {}", result2);
            logPosition("다시 읽기 결과 출력", byteBuffer);

            // rewind 가 되지 않았음으로 읽을 것이 없음.
            CharBuffer result3 = StandardCharsets.UTF_8.decode(byteBuffer);
            log.info("result: {}", result3);
            logPosition("rewind 가 되지 않았음으로 읽을 것이 없음.", byteBuffer);

            // buffer Clear
            byteBuffer.clear();
            logPosition("byte buffer clear", byteBuffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logPosition(String msg, ByteBuffer byteBuffer) {
        log.info("{}, position: {}, limit: {}, capacity: {}", msg, byteBuffer.position(),
            byteBuffer.limit(), byteBuffer.capacity());
    }

}
