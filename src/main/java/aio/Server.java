package aio;

import lombok.extern.slf4j.Slf4j;
import util.NetworkConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * 服务端
 *
 * @author tianxing
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(NetworkConfig.PORT));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                try {
                    serverSocketChannel.accept(attachment, this);
                    log.info("客户端: {}", socketChannel.getRemoteAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(NetworkConfig.BUFFER_SIZE);
                    socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer length, ByteBuffer byteBuffer) {
                            log.info("客户端发送的消息: {}", new String(byteBuffer.array(), 0, length, StandardCharsets.UTF_8));
                            socketChannel.write(ByteBuffer.wrap("hello aio client".getBytes(StandardCharsets.UTF_8)));
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
