package nio.udp;

import lombok.extern.slf4j.Slf4j;
import util.NetworkConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * 服务端
 *
 * @author tianxing
 */
@Slf4j
@SuppressWarnings("InfiniteLoopStatement")
public class Server {
    public static void main(String[] args) {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(NetworkConfig.PORT));
            ByteBuffer buffer = ByteBuffer.allocate(NetworkConfig.BUFFER_SIZE);
            while (true) {
                // 读取客户端发送的数据
                buffer.clear();
                SocketAddress socketAddress = channel.receive(buffer);
                buffer.flip();
                log.info("读取到客户端的数据: {}", new String(buffer.array(), 0, buffer.limit(), StandardCharsets.UTF_8));
                // 向客户端返回数据
                channel.send(ByteBuffer.wrap("hello datagram channel client".getBytes(StandardCharsets.UTF_8)),
                        socketAddress);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
