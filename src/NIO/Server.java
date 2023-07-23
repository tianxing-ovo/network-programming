package NIO;

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
public class Server {
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        //创建serverSocketChannel对象
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //设置为非阻塞的方式
        ssc.configureBlocking(false);
        //设置服务器端口号
        ssc.socket().bind(new InetSocketAddress(9000));
        //创建selector多路复用器
        Selector selector = Selector.open();
        //把ServerSocketChannel注册到selector上,并且对接收请求事件感兴趣
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            System.out.println("等待事件发生");
            //阻塞,轮询监听所有注册到selector上的channel
            selector.select();
            System.out.println("某个事件发生了");
            //获取所有发生事件的channel的SelectionKey
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            //迭代器
            Iterator<SelectionKey> iterator = selectionKeySet.iterator();
            while (iterator.hasNext()) {
                //每个SelectionKey对应一个channel
                SelectionKey selectionKey = iterator.next();
                handle(selectionKey); //处理SelectionKey
                iterator.remove(); //删除SelectionKey,防止重复处理
            }
        }
    }

    private static void handle(SelectionKey selectionKey) throws IOException {
        //是否是连接事件
        if (selectionKey.isAcceptable()) {
            System.out.println("有客户端连接了");
            //得到ServerSocketChannel,代表服务端
            ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
            //服务端接收客户端的连接,得到SocketChannel,建立服务端和客户端连接的通道(阻塞)
            SocketChannel socketChannel = ssc.accept();
            //设置成非阻塞
            socketChannel.configureBlocking(false);
            //把socketChannel注册到selector上,并且对读事件感兴趣
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        }
        //是否是读事件
        else if (selectionKey.isReadable()) {
            System.out.println("有客户端向服务端写数据");
            //得到ServerSocketChannel,代表服务端
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //通过SocketChannel把数据读到buffer中,返回读取的字节个数
            int len = socketChannel.read(buffer);
            if (len != -1) {
                System.out.println("读取到客户端的数据:" + new String(buffer.array(), 0, len));
            }
            //服务端返回数据给客户端
            ByteBuffer byteBuffer = ByteBuffer.wrap("Hello NIO".getBytes());
            //通过socketChannel写数据
            socketChannel.write(byteBuffer);
            //监听下一次事件,读或写
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }
}
