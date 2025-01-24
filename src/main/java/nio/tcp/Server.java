package nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 聊天室服务端
 *
 * @author tianxing
 */
@SuppressWarnings("InfiniteLoopStatement")
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(9000));
        Selector selector = Selector.open();
        // 监听服务端接收客户端连接事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            // 阻塞,轮询监听所有注册到selector上的channel
            int i = selector.select();
            if (i == 0) {
                continue;
            }
            // 获取所有发生事件的SelectionKey,1个SelectionKey对应1个channel
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    // 服务端接收客户端的连接,建立服务端和客户端连接的通道(阻塞)
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                    socketChannel.write(ByteBuffer.wrap("欢迎进入聊天室".getBytes(StandardCharsets.UTF_8)));
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len;
                    StringBuilder sb = new StringBuilder();
                    while ((len = socketChannel.read(buffer)) > 0) {
                        sb.append(new String(buffer.array(), 0, len, StandardCharsets.UTF_8));
                    }
                    if (sb.length() > 0) {
                        System.out.println(socketChannel.getRemoteAddress() + ":" + sb);
                        // 获取所有注册的SelectionKey
                        Set<SelectionKey> set = selectionKey.selector().keys();
                        // 广播消息给其它客户端
                        set.forEach(key -> {
                            if (key.channel() instanceof SocketChannel && key != selectionKey) {
                                SocketChannel channel = (SocketChannel) key.channel();
                                try {
                                    channel.write(ByteBuffer.wrap(sb.toString().getBytes(StandardCharsets.UTF_8)));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                    selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
                iterator.remove();
            }
        }
    }
}
