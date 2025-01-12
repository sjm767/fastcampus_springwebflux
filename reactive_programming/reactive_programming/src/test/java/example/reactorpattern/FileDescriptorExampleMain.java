package example.reactorpattern;

import java.io.File;
import java.util.Objects;

public class FileDescriptorExampleMain {
    public static void main(String[] args) {
        File file = new File(Objects.requireNonNull(FileDescriptorExampleMain.class
                        .getClassLoader()
                        .getResource("hello.txt"))
                .getFile());
    }

}
