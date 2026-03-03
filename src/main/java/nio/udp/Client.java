package nio.udp;

import lombok.extern.slf4j.Slf4j;
import util.NetworkConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * 客户端
 *
 * @author tianxing
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        try (DatagramChannel channel = DatagramChannel.open()) {
            // 向服务端发送数据
            InetSocketAddress address = new InetSocketAddress(NetworkConfig.HOST, NetworkConfig.PORT);
            channel.send(ByteBuffer.wrap("hello datagram channel server".getBytes(StandardCharsets.UTF_8)), address);
            // 读取服务端返回的数据
            ByteBuffer buffer = ByteBuffer.allocate(NetworkConfig.BUFFER_SIZE);
            channel.receive(buffer);
            buffer.flip();
            log.info("接收到服务端返回的数据: {}", new String(buffer.array(), 0, buffer.limit(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}