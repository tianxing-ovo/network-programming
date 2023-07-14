package BIO;

import java.io.IOException;
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
            Util.handle(socket);
        }
    }
}
