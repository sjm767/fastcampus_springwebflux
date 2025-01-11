package example.nio.filechannel;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileChannelReadWriteMain {

    public static void main(String[] args) {

        // READ
        File file = new File(Objects.requireNonNull(FileChannelReadWriteMain.class
                .getClassLoader()
                .getResource("file-channel.txt"))
            .getFile());

        try (FileChannel fileChannel = FileChannel.open(file.toPath())) {
            // Read
            ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
            fileChannel.read(readBuffer);

            readBuffer.flip();
            CharBuffer result = StandardCharsets.UTF_8.decode(readBuffer);
            log.info("읽기 결과: {}", result);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // WRITE
        File writeFile = new File(Objects.requireNonNull(FileChannelReadWriteMain.class
                .getClassLoader()
                .getResource("file-channel-write.txt"))
            .getFile());

        try (FileChannel writeFileChannel = FileChannel.open(writeFile.toPath(),
            StandardOpenOption.WRITE)) {
            ByteBuffer writeBuffer = ByteBuffer.wrap(
                "Hello World".getBytes(StandardCharsets.UTF_8));
            writeFileChannel.write(writeBuffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
