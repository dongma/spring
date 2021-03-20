package org.seckill.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Sam Ma
 * @date 2021/03/20
 * java nio client端的实现
 */
public class Client {

    public void start() throws IOException {
        // 打开socket通道
        SocketChannel sc = SocketChannel.open();
        // 设置为非阻塞
        sc.configureBlocking(false);
        // 连接服务器地址和端口
        sc.connect(new InetSocketAddress("localhost", 8001));
        // 打开选择器
        Selector selector = Selector.open();
        // 注册连接服务器socket的动作
        sc.register(selector, SelectionKey.OP_CONNECT);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            // 选择一组键，其相应的通道已为 I/O 操作准备就绪, 此方法执行处于阻塞模式的选择操作。
            selector.select();
            // 返回此选择器的已选择键集
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                // 判断此通道上是否正在进行连接操作
                if (key.isConnectable()) {
                    sc.finishConnect();
                    sc.register(selector, SelectionKey.OP_WRITE);
                    System.out.println("server connected...");
                    break;
                } else if (key.isWritable()) {
                    System.out.print("please input msg : ");
                    String message = scanner.nextLine();
                    ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                    System.out.println("sending: " + message);
                    sc.write(buffer);
                    sc.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    channel.read(buffer);
                    System.out.println("received: " + new String(buffer.array()).trim());
                }
            }
        }
    }

    /*public static void main(String[] args) throws IOException {
        new Client().start();
    }*/

}
