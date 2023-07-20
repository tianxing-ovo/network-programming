package NIO;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

public class Server {
    public static void main(String[] args) throws IOException {
        //创建serverSocketChannel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置为非阻塞的方式
        serverSocketChannel.configureBlocking(false);

    }
}
