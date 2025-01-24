package nio.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author tianxing
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        try (DatagramChannel channel = DatagramChannel.open()) {
            // 向服务端发送数据
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9000);
            channel.send(ByteBuffer.wrap("hello datagram channel server".getBytes()), address);
            // 读取服务端返回的数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.receive(buffer);
            buffer.flip();
            log.info("接收到服务端返回的数据: {}", new String(buffer.array(), 0, buffer.limit()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}