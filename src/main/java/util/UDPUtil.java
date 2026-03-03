package util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * UDP工具类
 *
 * @author tianxing
 */
@Slf4j
public class UDPUtil {

    /**
     * 读数据
     *
     * @param socket  套接字
     * @param message 消息
     * @return 数据报
     * @throws IOException IO异常
     */
    public static DatagramPacket read(DatagramSocket socket, String message) throws IOException {
        byte[] bytes = new byte[NetworkConfig.BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        // 阻塞 -> 直到接收到数据报
        socket.receive(packet);
        log.info("{}: {}", message, new String(bytes, 0, packet.getLength(), StandardCharsets.UTF_8));
        return packet;
    }

    /**
     * 写数据
     *
     * @param socket  套接字
     * @param data    数据
     * @param address 地址
     * @param port    端口
     * @throws IOException IO异常
     */
    public static void write(DatagramSocket socket, String data, InetAddress address, int port) throws IOException {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(packet);
    }
}
