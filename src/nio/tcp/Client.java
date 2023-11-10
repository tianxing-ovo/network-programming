package nio.tcp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@SuppressWarnings("InfiniteLoopStatement")
@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1", 9000));
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
            // 阻塞直到至少一个注册的SelectionKey变为就绪状态,返回就绪的SelectionKey的数量
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                if (selectionKey.isConnectable()) {
                    if (socketChannel.isConnectionPending()) {
                        socketChannel.finishConnect();
                        socketChannel.configureBlocking(false);
                        socketChannel.write(ByteBuffer.wrap("hello nio server".getBytes()));
                        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                    }
                } else if (selectionKey.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = socketChannel.read(buffer);
                    if (len != -1) {
                        log.info("服务端返回的数据是:{}", new String(buffer.array(), 0, len));
                    }
                }
                iterator.remove();
            }
        }
    }
}
