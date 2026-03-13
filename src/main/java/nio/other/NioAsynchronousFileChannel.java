package nio.other;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 异步文件通道
 *
 * @author tianxing
 */
public class NioAsynchronousFileChannel {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path path = Paths.get("src/main/resources/txt/async-file-channel.txt");
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
            ByteBuffer buffer = ByteBuffer.wrap("Hello AsynchronousFileChannel".getBytes(StandardCharsets.UTF_8));
            channel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer length, ByteBuffer byteBuffer) {
                    System.out.println(new String(byteBuffer.array(), 0, length, StandardCharsets.UTF_8));
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("exc: " + exc);
                }
            });
        }
    }
}