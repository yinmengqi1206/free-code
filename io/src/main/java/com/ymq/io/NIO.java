package com.ymq.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Set;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2022/9/16 12:45
 */
@Slf4j
public class NIO {

    @SneakyThrows
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(1206));
        //设置非阻塞
        serverSocketChannel.configureBlocking(false);
        //注册读事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            if(selector.select()==0){
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey: selectionKeys) {
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
                    socketChannel.configureBlocking(false);
                    log.info("客户端连接{}",socketChannel.getRemoteAddress());
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    byteBuffer.clear();
                    if(channel.read(byteBuffer)<=0){
                        continue;
                    }
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.limit()];
                    byteBuffer.get(bytes);
                    log.info("{}发送数据:{}",channel.getRemoteAddress(),new String(bytes));
                    if(Objects.equals("exit",new String(bytes).trim())){
                        channel.close();
                        continue;
                    }
                    byteBuffer.clear();byteBuffer.put(bytes);byteBuffer.flip();
                    channel.write(byteBuffer);
                }
            }
            selectionKeys.clear();
        }
    }
}
