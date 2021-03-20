package org.seckill.nio;

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
 * @author Sam Ma
 * @date 2021/03/20
 * 使用java nio实现server端，用ServerSocketChannel、Selector实现
 */
public class Server {

    /* 定时轮询器linux用epoll实现，依赖于操作系统底实现 */
    private Selector selector;

    /* server端启动线程，SocketChannel bind本地端口，根据Selector建立连接 */
    public void start() throws IOException {
        // 打开服务器套接字通道socketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 服务器配置为非阻塞non-blocking, 并绑定到本机的8081端口
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress("127.0.0.1", 8001));

        // 通过open()方法找到Selector, 注册到selector等待连接
        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (!Thread.currentThread().isInterrupted()) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    readMsg(key);
                } else if (key.isWritable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    String msg = "{from server} for test";
                    channel.write(ByteBuffer.wrap(msg.getBytes()));
                }
                keyIterator.remove(); // 该事件已经处理，可以丢弃
            }
        }
    }

    /* 内核态将数据准备好后，selector查看到状态正常，开始将数据写入缓冲区 */
    private void readMsg(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int i = channel.read(buffer);
        if (i != -1) {  //用于判断客户端是否断开了连接
            String msg = new String(buffer.array()).trim();
            System.out.println("server received message: " + msg);
        } else {
            channel.close(); //如果客户端断开连接就关闭该连接
        }
        channel.register(selector, SelectionKey.OP_WRITE);
    }

    /* SelectionKey是用于建立连接 */
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = ssc.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("a new client connected " + clientChannel.getRemoteAddress());
    }

   /* public static void main(String[] args) throws IOException {
        System.out.println("server started ....");
        new Server().start();
    }*/

}
