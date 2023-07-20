package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * 服务端
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //创建serverSocketChannel对象
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //设置为非阻塞的方式
        ssc.configureBlocking(false);
        //设置服务器端口号
        ssc.socket().bind(new InetSocketAddress(9000));
        //创建selector多路复用器
        Selector selector = Selector.open();
        //把ServerSocketChannel注册到selector上,并且对接收请求感兴趣
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {

        }
    }
}
