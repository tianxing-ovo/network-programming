package bio.udp;


import util.UDPUtil;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 客户端
 *
 * @author tianxing
 */
public class Client {
    public static void main(String[] args) throws Exception {
        // 获取本地回环地址(127.0.0.1)
        InetAddress address = InetAddress.getByName("127.0.0.1");
        DatagramSocket socket = new DatagramSocket();
        // 向服务端发送数据
        UDPUtil.write(socket, "hello udp server", address, 9000);
        // 读取服务端返回的数据
        UDPUtil.read(socket, "接收到服务端返回的数据");
        // 关闭socket
        socket.close();
    }
}
