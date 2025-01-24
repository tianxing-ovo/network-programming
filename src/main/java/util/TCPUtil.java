package util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * TCP工具类
 *
 * @author tianxing
 */
@Slf4j
public class TCPUtil {

    /**
     * 读数据
     *
     * @param socket  套接字
     * @param message 消息
     * @throws IOException IO异常
     */
    public static void read(Socket socket, String message) throws IOException {
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        // 阻塞 -> 直到I/O流通道中有数据
        int len = is.read(bytes);
        log.info("{}: {}", message, new String(bytes, 0, len));
    }

    /**
     * 写数据
     *
     * @param socket 套接字
     * @param data   数据
     * @throws IOException IO异常
     */
    public static void write(Socket socket, String data) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(data.getBytes());
        // 将缓冲区的数据立即写入到目标流中 -> 不需要等待缓冲区填满或达到特定条件
        os.flush();
    }
}
