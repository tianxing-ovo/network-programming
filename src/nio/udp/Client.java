package nio.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        ByteBuffer buffer = ByteBuffer.wrap("hello datagram channel server".getBytes());
        channel.send(buffer, new InetSocketAddress("127.0.0.1", 9000));
        channel.close();
    }
}