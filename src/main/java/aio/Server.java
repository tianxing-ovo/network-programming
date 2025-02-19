package aio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author tianxing
 */
public class Server {
    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9000));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                try {
                    serverSocketChannel.accept(attachment, this);
                    System.out.println("客户端: " + socketChannel.getRemoteAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer length, ByteBuffer byteBuffer) {
                            System.out.println("客户端发送的消息: " + new String(buffer.array(), 0, length));
                            socketChannel.write(ByteBuffer.wrap("hello aio client".getBytes()));
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {

                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
        Thread.sleep(Integer.MAX_VALUE);
        serverSocketChannel.close();
    }
}
