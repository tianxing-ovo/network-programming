package nio.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

@Slf4j
@SuppressWarnings("InfiniteLoopStatement")
public class Server {
    public static void main(String[] args) {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(9000));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                // 读取客户端发送的数据
                buffer.clear();
                SocketAddress socketAddress = channel.receive(buffer);
                buffer.flip();
                log.info("读取到客户端的数据:{}", new String(buffer.array(), 0, buffer.limit()));
                // 向客户端返回数据
                channel.send(ByteBuffer.wrap("hello datagram channel client".getBytes()), socketAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
