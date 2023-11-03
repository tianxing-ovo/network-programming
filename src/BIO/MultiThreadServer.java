package BIO;


import io.github.tianxingovo.common.SocketUtil;
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
                    SocketUtil.process(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
