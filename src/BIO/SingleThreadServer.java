package BIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 单线程的服务端
 */
public class SingleThreadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);//服务套接字,9000端口
        while (true) {
            System.out.println("等待客户端的连接...");
            //阻塞等待客户端的连接
            Socket socket = serverSocket.accept();
            System.out.println("已有客户端连接");
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
}
