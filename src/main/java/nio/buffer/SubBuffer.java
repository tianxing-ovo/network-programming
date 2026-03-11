package nio.buffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 子缓冲区
 *
 * @author tianxing
 */
public class SubBuffer {
    public static void main(String[] args) {
        // 创建原始缓冲区
        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        // 设置可操作数据范围: [3,7)
        buffer.position(3);
        buffer.limit(7);
        // 创建子缓冲区(共享相同的底层数据数组)
        ByteBuffer subBuffer = buffer.slice();
        // 子缓冲区: [pos=0 lim=4 cap=4]
        System.out.println("子缓冲区: " + subBuffer);
        // 更改子缓冲区的内容(原始缓冲区的内容也会改变)
        subBuffer.put(0, (byte) 40);
        // 原始缓冲区的内容: [1, 2, 3, 40, 5, 6, 7, 8, 9]
        System.out.println("原始缓冲区的内容: " + Arrays.toString(buffer.array()));
        // 子缓冲区的内容: [1, 2, 3, 40, 5, 6, 7, 8, 9]
        System.out.println("子缓冲区的内容: " + Arrays.toString(subBuffer.array()));
    }
}