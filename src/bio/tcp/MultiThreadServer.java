package bio.tcp;


import io.github.tianxingovo.bio.TCPUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 多线程的服务端
 */
@Slf4j
@SuppressWarnings("InfiniteLoopStatement")
public class MultiThreadServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            log.info("等待客户端的连接...");
            // 阻塞等待客户端的连接
            Socket socket = serverSocket.accept();
            log.info("已有客户端连接");
            new Thread(() -> {
                try {
                    TCPUtil.read(socket, "接收到客户端发送的数据");
                    TCPUtil.write(socket, "hello tcp client");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
