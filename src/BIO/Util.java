package BIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Util {

    public static void handle(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();//获取输入流
        byte[] bytes = new byte[1024];
        //阻塞等待客户端向I/O流通道中写数据
        int len = is.read(bytes);
        System.out.println("收到客户端的数据:" + new String(bytes, 0, len));
        //返回信息给客户端
        OutputStream os = socket.getOutputStream();//获取输出流
        os.write("success".getBytes());
        os.flush();//将缓冲区的数据立即写入到目标流中,而不需要等待缓冲区填满或达到特定条件
    }
}
