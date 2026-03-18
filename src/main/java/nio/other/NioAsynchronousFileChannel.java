package nio.other;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

/**
 * 异步文件通道
 *
 * @author tianxing
 */
public class NioAsynchronousFileChannel {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path path = Paths.get("src/main/resources/txt/async-file-channel.txt");
        // 异步读取文件
        read(path);
        // 异步写入文件
        write(path);
    }

    public static void read(Path path) {
        CountDownLatch latch = new CountDownLatch(1);
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 异步读取文件
            fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    try {
                        if (result == -1) {
                            System.out.println("未读取到数据");
                            return;
                        }
                        System.out.println("读取到的字节数: " + result);
                        // 切换为读取模式
                        attachment.flip();
                        System.out.println("读取到的内容: " + StandardCharsets.UTF_8.decode(attachment));
                    } finally {
                        latch.countDown();
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        System.out.println("读取失败: " + exc.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
            // 等待异步读取完成
            latch.await();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(Path path) {
        CountDownLatch latch = new CountDownLatch(1);
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            ByteBuffer buffer = ByteBuffer.wrap("Hello AsynchronousFileChannel".getBytes(StandardCharsets.UTF_8));
            // 异步写入文件
            fileChannel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    try {
                        System.out.println("写入的字节数: " + result);
                    } finally {
                        latch.countDown();
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        System.out.println("写入失败: " + exc.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
            // 等待异步写入完成
            latch.await();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}