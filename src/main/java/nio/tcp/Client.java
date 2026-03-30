package nio.tcp;

import lombok.extern.slf4j.Slf4j;
import util.NetworkConfig;

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
@Slf4j
public class Client {

    public static void main(String[] args) throws IOException {
        System.out.print("请输入您的昵称: ");
        String name = new Scanner(System.in).nextLine();
        start(name);
    }

    /**
     * 启动客户端
     *
     * @param name 客户端姓名
     * @throws IOException IO异常
     */
    public static void start(String name) throws IOException {
        // 创建SocketChannel并连接服务器
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(NetworkConfig.HOST, NetworkConfig.PORT));
        log.info("已连接到服务器: {}", channel.getRemoteAddress());
        // 设置为非阻塞模式(配合Selector实现事件驱动的多路复用I/O)
        channel.configureBlocking(false);
        // 创建Selector用于监听通道的I/O事件
        Selector selector = Selector.open();
        // 注册读事件到Selector(监听服务器发来的消息)
        channel.register(selector, SelectionKey.OP_READ);
        // 启动发送线程(处理用户输入和消息发送)
        Thread sendThread = getThread(name, channel);
        sendThread.start();
        // 主线程(处理服务器消息接收)
        while (true) {
            // 阻塞等待至少一个注册的Channel有I/O事件发生(返回就绪的SelectionKey的数量)
            int i = selector.select();
            if (i == 0) {
                continue;
            }
            log.info("唤醒成功 | 就绪数量: {} | 总连接数: {}", i, selector.keys().size());
            // 获取所有发生事件的SelectionKey(1个SelectionKey对应1个channel)
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 处理读事件(服务端发送消息)
                if (selectionKey.isReadable()) {
                    // 获取与服务端通信的SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(NetworkConfig.BUFFER_SIZE);
                    StringBuilder sb = new StringBuilder();
                    int len;
                    try {
                        // 循环读取数据直到读完或连接关闭
                        while ((len = socketChannel.read(buffer)) > 0) {
                            sb.append(new String(buffer.array(), 0, len, StandardCharsets.UTF_8));
                            // 清空缓冲区准备下次读取
                            buffer.clear();
                        }
                        // 读取到-1表示服务器关闭了连接
                        if (len == -1) {
                            System.out.println("服务器已关闭连接");
                            closeClient(selector, socketChannel);
                            return;
                        }
                    } catch (IOException e) {
                        // 处理连接异常
                        System.out.println("与服务器的连接已断开: " + e.getMessage());
                        closeClient(selector, socketChannel);
                        return;
                    }
                    // 输出接收到的消息
                    if (sb.length() > 0) {
                        System.out.println(sb);
                    }
                }
                // 移除已处理的SelectionKey(避免重复处理)
                iterator.remove();
            }
        }
    }

    /**
     * 获取发送线程
     *
     * @param name          客户端姓名
     * @param socketChannel 客户端通道
     * @return 发送线程
     */
    private static Thread getThread(String name, SocketChannel socketChannel) {
        Thread sendThread = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (sc.hasNext()) {
                String s = name + ":" + sc.nextLine();
                try {
                    socketChannel.write(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
                } catch (IOException e) {
                    System.out.println("消息发送失败");
                    return;
                }
            }
        });
        // 设置为守护线程(主线程退出时自动结束)
        sendThread.setDaemon(true);
        return sendThread;
    }

    /**
     * 关闭客户端连接
     *
     * @param selector      选择器
     * @param socketChannel 客户端通道
     * @throws IOException IO异常
     */
    private static void closeClient(Selector selector, SocketChannel socketChannel) throws IOException {
        selector.close();
        socketChannel.close();
    }
}
