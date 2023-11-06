package nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1", 9000));
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
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
                        socketChannel.register(selector, SelectionKey.OP_READ);
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
