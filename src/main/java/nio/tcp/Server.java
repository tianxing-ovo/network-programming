package nio.tcp;

import lombok.extern.slf4j.Slf4j;
import util.NetworkConfig;

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
@Slf4j
@SuppressWarnings({"InfiniteLoopStatement", "resource"})
public class Server {

    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel用于监听客户端连接
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置为非阻塞模式(配合Selector实现事件驱动的多路复用I/O)
        ssc.configureBlocking(false);
        // 绑定监听端口(等待客户端连接)
        ssc.bind(new InetSocketAddress(NetworkConfig.PORT));
        // 创建Selector用于管理多个Channel的I/O事件
        Selector selector = Selector.open();
        // 注册接收连接事件到Selector(监听新的客户端连接)
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        // 持续监听和处理I/O事件
        while (true) {
            // 阻塞等待至少一个注册的Channel有I/O事件发生(返回就绪的SelectionKey数量)
            int i = selector.select();
            if (i == 0) {
                continue;
            }
            log.info("唤醒成功 | 就绪数量: {} | 总连接数: {}", i, selector.keys().size() - 1);
            // 获取所有发生事件的SelectionKey(1个SelectionKey对应1个channel)
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 处理接收新连接事件
                if (selectionKey.isAcceptable()) {
                    // 获取ServerSocketChannel(监听socket)
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    // 非阻塞接受客户端连接(返回与客户端通信的SocketChannel)
                    SocketChannel socketChannel = channel.accept();
                    if (socketChannel == null) {
                        iterator.remove();
                        continue;
                    }
                    // 设置客户端通道为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 注册读事件到Selector(监听客户端发送的消息)
                    socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                    log.info("{}: 连接成功", socketChannel.getRemoteAddress());
                    // 向客户端发送欢迎消息
                    socketChannel.write(ByteBuffer.wrap("欢迎进入聊天室".getBytes(StandardCharsets.UTF_8)));
                }
                // 处理读事件(客户端发送消息)
                else if (selectionKey.isReadable()) {
                    // 获取与客户端通信的SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 创建缓冲区接收客户端数据
                    ByteBuffer buffer = ByteBuffer.allocate(NetworkConfig.BUFFER_SIZE);
                    StringBuilder sb = new StringBuilder();
                    int len;
                    try {
                        // 循环读取客户端数据直到读完或连接关闭
                        while ((len = socketChannel.read(buffer)) > 0) {
                            sb.append(new String(buffer.array(), 0, len, StandardCharsets.UTF_8));
                            buffer.clear();
                        }
                        // 读取到-1表示客户端关闭了连接
                        if (len == -1) {
                            closeClient(selectionKey, socketChannel, "客户端已断开连接");
                            iterator.remove();
                            continue;
                        }
                    } catch (IOException e) {
                        // 处理读取异常(客户端异常断开)
                        closeClient(selectionKey, socketChannel, "客户端连接异常断开");
                        iterator.remove();
                        continue;
                    }
                    // 处理接收到的消息
                    if (sb.length() > 0) {
                        // 输出客户端地址和消息内容
                        System.out.println(socketChannel.getRemoteAddress() + ":" + sb);
                        // 将消息广播给所有其他客户端
                        broadcastMessage(selectionKey, sb.toString());
                    }
                }
                // 移除已处理的SelectionKey(避免重复处理)
                iterator.remove();
            }
        }
    }

    /**
     * 广播消息给所有其他客户端
     *
     * @param currentKey 当前SelectionKey
     * @param message    要广播的消息
     */
    private static void broadcastMessage(SelectionKey currentKey, String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        Set<SelectionKey> keys = currentKey.selector().keys();
        for (SelectionKey key : keys) {
            if (!(key.channel() instanceof SocketChannel) || key == currentKey) {
                continue;
            }
            SocketChannel channel = (SocketChannel) key.channel();
            try {
                channel.write(ByteBuffer.wrap(bytes));
            } catch (IOException e) {
                closeClient(key, channel, "广播消息失败，客户端连接已关闭");
            }
        }
    }

    /**
     * 关闭客户端连接
     *
     * @param selectionKey  选择器键
     * @param socketChannel 客户端通道
     * @param message       关闭连接的消息
     */
    private static void closeClient(SelectionKey selectionKey, SocketChannel socketChannel, String message) {
        try {
            System.out.println(getClientAddress(socketChannel) + ": " + message);
            selectionKey.cancel();
            socketChannel.close();
        } catch (IOException e) {
            log.error("关闭客户端连接失败: {}", e.getMessage());
        }
    }

    /**
     * 获取客户端地址
     *
     * @param socketChannel 客户端通道
     * @return 客户端地址
     */
    private static String getClientAddress(SocketChannel socketChannel) {
        try {
            return socketChannel.getRemoteAddress().toString();
        } catch (IOException e) {
            return "未知客户端";
        }
    }
}
