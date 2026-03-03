package aio;

import lombok.extern.slf4j.Slf4j;
import util.NetworkConfig;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 客户端
 *
 * @author tianxing
 */
@Slf4j
public class Client {
    public static void main(String[] args) throws Exception {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        // 阻塞等待连接完成
        socketChannel.connect(new InetSocketAddress(NetworkConfig.HOST, NetworkConfig.PORT)).get();
        socketChannel.write(ByteBuffer.wrap("hello aio server".getBytes(StandardCharsets.UTF_8)));
        ByteBuffer buffer = ByteBuffer.allocate(NetworkConfig.BUFFER_SIZE);
        int len;
        while ((len = socketChannel.read(buffer).get()) > 0) {
            log.info("服务端返回的消息: {}", new String(buffer.array(), 0, len, StandardCharsets.UTF_8));
        }
        socketChannel.close();
    }
}
