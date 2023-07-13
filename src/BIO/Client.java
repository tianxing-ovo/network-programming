package BIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端
 */
public class Client {
    public static void main(String[] args) throws IOException {
        //连接服务端
        Socket socket = new Socket("localhost", 9000);
        //发送数据
        OutputStream os = socket.getOutputStream();//获取输出流
        os.write("bio data from client".getBytes());
        os.flush();
        //接收服务端返回的信息
        InputStream is = socket.getInputStream();//获取输入流
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        System.out.println("接收到服务端返回的数据:" + new String(bytes, 0, len));
        //关闭连接
        socket.close();
    }
}
