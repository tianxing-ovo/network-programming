package bio.udp;


import lombok.extern.slf4j.Slf4j;
import util.UDPUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author tianxing
 */
@SuppressWarnings("InfiniteLoopStatement")
@Slf4j
public class Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(9000);
        while (true) {
            // 读取客户端发送的数据
            DatagramPacket packet = UDPUtil.read(socket, "接收到客户端发送的数据");
            // 向客户端返回数据
            UDPUtil.write(socket, "hello udp client", packet.getAddress(), packet.getPort());
        }
    }
}
