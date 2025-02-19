package aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author tianxing
 */
public class Client {
    public static void main(String[] args) throws Exception {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        // 阻塞等待连接完成
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9000)).get();
        socketChannel.write(ByteBuffer.wrap("hello aio server".getBytes()));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int len;
        while ((len = socketChannel.read(buffer).get()) > 0) {
            System.out.println("服务端返回的消息: " + new String(buffer.array(), 0, len));
        }
        socketChannel.close();
    }
}
