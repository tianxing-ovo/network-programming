package bio;

import io.github.tianxingovo.common.SocketUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 单线程的服务端
 */
@Slf4j
@SuppressWarnings("InfiniteLoopStatement")
public class SingleThreadServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            log.info("等待客户端的连接...");
            // 阻塞等待客户端的连接
            Socket socket = serverSocket.accept();
            log.info("已有客户端连接");
            SocketUtil.process(socket);
        }
    }
}
