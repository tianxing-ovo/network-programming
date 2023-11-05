package nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务端
 */
@SuppressWarnings("InfiniteLoopStatement")
@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); // 非阻塞模式
        ssc.socket().bind(new InetSocketAddress(9000)); // 设置服务器端口
        Selector selector = Selector.open();
        // ServerSocketChannel注册到selector上,监听服务端接收客户端连接事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            log.info("等待事件发生");
            // 阻塞,轮询监听所有注册到selector上的channel
            selector.select();
            log.info("某个事件发生了");
            // 获取所有发生事件的channel,1个SelectionKey对应1个channel
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                process(selectionKey);
                iterator.remove();
            }
        }
    }

    private static void process(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            log.info("有客户端连接了");
            // 得到ServerSocketChannel,代表服务端
            ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
            // 服务端接收客户端的连接,建立服务端和客户端连接的通道(阻塞)
            SocketChannel socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            // 把socketChannel注册到selector上,服务端监听读事件
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            log.info("有客户端向服务端写数据");
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = socketChannel.read(buffer);
            if (len != -1) {
                log.info("读取到客户端的数据:{}", new String(buffer.array(), 0, len));
            }
            socketChannel.write(ByteBuffer.wrap("hello nio".getBytes()));
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }
}
