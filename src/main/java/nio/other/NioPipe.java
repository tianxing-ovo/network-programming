package nio.other;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;

/**
 * 管道(两个线程之间进行单向数据传输)
 *
 * @author tianxing
 */
public class NioPipe {

    public static void main(String[] args) throws IOException {
        // 获取管道
        Pipe pipe = Pipe.open();
        // 创建并启动写线程
        new Thread(new WriterTask(pipe), "pipe-writer").start();
        // 创建并启动读线程
        new Thread(new ReaderTask(pipe), "pipe-reader").start();
    }

    static class WriterTask implements Runnable {
        private final Pipe pipe;

        WriterTask(Pipe pipe) {
            this.pipe = pipe;
        }

        @Override
        public void run() {
            try (Pipe.SinkChannel sinkChannel = pipe.sink()) {
                ByteBuffer buffer = ByteBuffer.wrap("hello pipe".getBytes(StandardCharsets.UTF_8));
                while (buffer.hasRemaining()) {
                    sinkChannel.write(buffer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class ReaderTask implements Runnable {
        private final Pipe pipe;

        ReaderTask(Pipe pipe) {
            this.pipe = pipe;
        }

        @Override
        public void run() {
            try (Pipe.SourceChannel sourceChannel = pipe.source()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int len;
                while ((len = sourceChannel.read(buffer)) > 0) {
                    System.out.println(Thread.currentThread().getName() + ": " +
                            new String(buffer.array(), 0, len, StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}