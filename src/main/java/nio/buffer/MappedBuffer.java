package nio.buffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射缓冲区
 *
 * @author tianxing
 */
public class MappedBuffer {

    public static void main(String[] args) throws IOException {
        // 指定要映射的文件路径
        File file = new File("src/main/resources/txt/1.txt");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
             FileChannel channel = randomAccessFile.getChannel()) {
            // 创建内存映射缓冲区
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
            // 读取并打印文件内容
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
        }
    }
}