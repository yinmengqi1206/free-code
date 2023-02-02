package com.ymq.server;

import com.ymq.encoder.CustomEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * http://blog.csdn.net/linuu/article/details/51371595
 * http://blog.163.com/linfenliang@126/blog/static/127857195201210821145721/
 *
 * 重连
 * http://blog.csdn.net/z69183787/article/details/52625095
 * http://blog.csdn.net/chdhust/article/details/51649184
 */
public class ServerApp {

    /*
    maxFrameLength：解码的帧的最大长度
    lengthFieldOffset ：长度属性的起始位（偏移位），包中存放有整个大数据包长度的字节，这段字节的其实位置
    lengthFieldLength：长度属性的长度，即存放整个大数据包长度的字节所占的长度
    lengthAdjustmen：长度调节值，在总长被定义为包含包头长度时，修正信息长度。
    initialBytesToStrip：跳过的字节数，根据需要我们跳过lengthFieldLength个字节，以便接收端直接接受到不含“长度属性”的内容
    failFast ：为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
     */
    private static final int MAX_FRAME_LENGTH = 1024 * 1024 * 10;
    private static final int LENGTH_FIELD_OFFSET = 2;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

    private int port;

    public ServerApp(int port) {
        this.port = port;
    }

    public void start() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap sbs = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {

                            /**
                             * int maxFrameLength
                             * int lengthFieldOffset
                             * int lengthFieldLength
                             * int lengthAdjustment
                             * int initialBytesToStrip
                             * boolean failFast
                             */
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(
                                    MAX_FRAME_LENGTH,
                                    LENGTH_FIELD_OFFSET,
                                    LENGTH_FIELD_LENGTH,
                                    LENGTH_ADJUSTMENT,
                                    INITIAL_BYTES_TO_STRIP,
                                    false));

                            ch.pipeline().addLast(new CustomServerHandler());

                            ch.pipeline().addLast(new CustomEncoder());

                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            System.out.println("绑定端口,开始接收TCP连接");
            ChannelFuture future = sbs.bind(port).sync();

            System.out.println("服务监听端口:" + port);
            future.channel().closeFuture().sync();

            System.out.println("Server Exit");

        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        int port;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }

        new ServerApp(port).start();
    }
}
