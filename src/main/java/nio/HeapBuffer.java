package nio;

import java.nio.ByteBuffer;

/**
 * 堆内缓冲区
 *
 * @author tianxing
 */
public class HeapBuffer {
    public static void main(String[] args) {
        // 创建堆内缓冲区
        ByteBuffer heapBuffer = ByteBuffer.allocate(10);
        // 写入前: [pos=0 lim=10 cap=10]
        System.out.println("写入前: " + heapBuffer);
        // 写入数据
        for (int i = 0; i < heapBuffer.capacity(); i++) {
            heapBuffer.put((byte) (i + 1));
        }
        // 写入后: [pos=10 lim=10 cap=10]
        System.out.println("写入后: " + heapBuffer);
        // 切换到读模式(pos=0)
        heapBuffer.flip();
        // 读取前: [pos=0 lim=10 cap=10]
        System.out.println("读取前: " + heapBuffer);
        // 1 2 3 4 5 6 7 8 9 10
        while (heapBuffer.hasRemaining()) {
            System.out.print(heapBuffer.get() + (heapBuffer.hasRemaining() ? " " : ""));
        }
        // 读取后: [pos=10 lim=10 cap=10]
        System.out.println("\n读取后: " + heapBuffer);
    }
}