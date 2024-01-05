package com.ymq;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.SneakyThrows;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2023/1/17 14:50
 */
public class HttpServer {

    /*
    maxFrameLength：解码的帧的最大长度
    lengthFieldOffset ：长度属性的起始位（偏移位），包中存放有整个大数据包长度的字节，这段字节的其实位置
    lengthFieldLength：长度属性的长度，即存放整个大数据包长度的字节所占的长度
    lengthAdjustmen：长度调节值，在总长被定义为包含包头长度时，修正信息长度。
    initialBytesToStrip：跳过的字节数，根据需要我们跳过lengthFieldLength个字节，以便接收端直接接受到不含“长度属性”的内容
    failFast ：为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
     */
    private static final int MAX_FRAME_LENGTH = 1024 * 1024 * 10;
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int LENGTH_FIELD_LENGTH = 2;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

    @SneakyThrows
    public void start(int port) {
        EventLoopGroup boos = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap()
                    .group(boos, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer("&".getBytes());
                            socketChannel.pipeline()
                                    //.addLast(new FixedLengthFrameDecoder(10))
                                    .addLast(new DelimiterBasedFrameDecoder(10, true, true, delimiter));
                            socketChannel.pipeline()
                                    .addLast("codec", new HttpServerCodec())
                                    // HttpContent 压缩
                                    .addLast("compressor", new HttpContentCompressor())
                                    // HTTP 消息聚合
                                    .addLast("aggregator", new HttpObjectAggregator(65536))
                                    // 自定义业务逻辑处理器
                                    .addLast("handler", new HttpServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = server.bind().sync();
            System.out.println("Http Server started， Listening on " + port);
            f.channel().closeFuture().sync();
        } finally {
            boos.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpServer().start(9999);
    }
}
