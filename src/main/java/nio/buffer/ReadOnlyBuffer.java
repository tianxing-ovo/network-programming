package nio.buffer;

import java.nio.ByteBuffer;

/**
 * 只读缓冲区
 *
 * @author tianxing
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        // 创建原始缓冲区
        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        // 创建只读缓冲区(共享相同的底层数据数组)
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        // 修改原始缓冲区的数据
        buffer.put(0, (byte) 40);
        // 40 2 3 4 5 6 7 8 9
        while (readOnlyBuffer.hasRemaining()) {
            System.out.print(readOnlyBuffer.get() + (readOnlyBuffer.hasRemaining() ? " " : ""));
        }
    }
}
