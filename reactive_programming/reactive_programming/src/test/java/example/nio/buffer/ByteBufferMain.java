package example.nio.buffer;

import example.io.inputstream.FIleInputStreamExample;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferMain {

    public static void main(String[] args) {
        ByteBuffer heapByteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(1024);

        log.info("heapByteBuffer isDirect: {}", heapByteBuffer.isDirect());
        log.info("directByteBuffer isDirect: {}", directByteBuffer.isDirect());

        File file = new File(Objects.requireNonNull(ByteBufferMain.class
                .getClassLoader()
                .getResource("file-channel.txt"))
            .getFile());

        try (FileChannel fileChannel = FileChannel.open(file.toPath())) {

            fileChannel.read(heapByteBuffer);
            heapByteBuffer.flip();
            log.info("heapByteBuffer로 읽기");
            CharBuffer heapByteBufferResult = StandardCharsets.UTF_8.decode(heapByteBuffer);
            log.info("heapByteBufferResult: {}", heapByteBufferResult);

            // 포지션을 0으로 초기화
            fileChannel.position(0);

            log.info("directByteBuffer로 읽기");
            fileChannel.read(directByteBuffer);
            directByteBuffer.flip();
            CharBuffer directByteBufferResult = StandardCharsets.UTF_8.decode(directByteBuffer);
            log.info("directByteBufferResult: {}", directByteBufferResult);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            heapByteBuffer.clear();
            directByteBuffer.clear();
        }
    }
}
