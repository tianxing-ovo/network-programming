package nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 聊天室客户端
 *
 * @author tianxing
 */
@SuppressWarnings("InfiniteLoopStatement")
public class Client {

    public static void start(String name) throws IOException {
        SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9000));
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()) {
                String s = name + ":" + sc.nextLine();
                try {
                    channel.write(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        while (true) {
            // 阻塞直到至少一个注册的SelectionKey变为就绪状态,返回就绪的SelectionKey的数量
            int i = selector.select();
            if (i == 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                if (selectionKey.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len;
                    StringBuilder sb = new StringBuilder();
                    while ((len = socketChannel.read(buffer)) > 0) {
                        sb.append(new String(buffer.array(), 0, len, StandardCharsets.UTF_8));
                    }
                    System.out.println(sb);
                    socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                }
                iterator.remove();
            }
        }
    }
}
