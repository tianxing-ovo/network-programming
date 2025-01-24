package bio.tcp;


import lombok.extern.slf4j.Slf4j;
import util.TCPUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * 客户端
 *
 * @author tianxing
 */
@Slf4j
public class Client {

    public static void main(String[] args) throws IOException {
        // 连接服务端
        Socket socket = new Socket("localhost", 9000);
        // 发送数据
        TCPUtil.write(socket, "hello tcp server");
        // 接收服务端返回的信息
        TCPUtil.read(socket, "接收到服务端返回的数据");
        // 关闭连接
        socket.close();
    }
}
