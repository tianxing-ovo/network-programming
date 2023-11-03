package BIO;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端
 */
@Slf4j
public class Client {

    public static void main(String[] args) throws IOException {
        // 连接服务端
        Socket socket = new Socket("localhost", 9000);
        // 发送数据
        OutputStream os = socket.getOutputStream();
        os.write("bio data from client".getBytes());
        os.flush();
        // 接收服务端返回的信息
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        log.info("接收到服务端返回的数据:{}", new String(bytes, 0, len));
        // 关闭连接
        socket.close();
    }
}
