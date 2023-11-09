package io.github.tianxingovo.nio.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(9000));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.receive(buffer);
        buffer.flip();
        log.info("服务端收到数据:{}", new String(buffer.array(), 0, buffer.limit()));
        channel.close();
    }
}