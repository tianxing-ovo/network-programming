package bio.tcp;


import lombok.extern.slf4j.Slf4j;
import util.TCPUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 多线程的服务端
 *
 * @author tianxing
 */
@Slf4j
@SuppressWarnings("InfiniteLoopStatement")
public class MultiThreadServer {

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(9000)) {
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
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        }
    }
}
