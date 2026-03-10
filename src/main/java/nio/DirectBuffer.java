package nio;

import java.nio.ByteBuffer;

/**
 * 直接缓冲区
 *
 * @author tianxing
 */
public class DirectBuffer {
    public static void main(String[] args) {
        // 创建直接缓冲区
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(10);
        // 写入数据
        for (int i = 0; i < directBuffer.capacity(); i++) {
            directBuffer.put((byte) (i + 1));
        }
        // 切换到读模式(pos=0)
        directBuffer.flip();
        // 1 2 3 4 5 6 7 8 9 10
        while (directBuffer.hasRemaining()) {
            System.out.print(directBuffer.get() + (directBuffer.hasRemaining() ? " " : ""));
        }
    }
}