package nio.other;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;

/**
 * 文件锁(控制多个进程对文件的访问)
 *
 * @author tianxing
 */
public class NioFileLock {
    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File("src/main/resources/txt/file-lock.txt");
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            FileChannel fileChannel = raf.getChannel();
            // 获取排它锁
            FileLock exclusiveLock = fileChannel.lock(0, file.length(), false);
            // 清空旧内容后再写
            fileChannel.truncate(0);
            int written = fileChannel.write(ByteBuffer.wrap("Hello FileLock".getBytes(StandardCharsets.UTF_8)));
            // 将文件指针重置到开头
            fileChannel.position(0);
            System.out.println("实际写入字节数: " + written);
            exclusiveLock.release();
            // 获取共享锁
            FileLock sharedLock = fileChannel.lock(0, file.length(), true);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = fileChannel.read(buffer);
            System.out.println("实际读取字节数: " + read);
            System.out.println("读取到的内容: " + new String(buffer.array(), 0, read, StandardCharsets.UTF_8));
            sharedLock.release();
        }
    }
}