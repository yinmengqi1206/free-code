package com.ymq.client;

import com.ymq.protocol.CustomMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CustomClientHandler extends ChannelInboundHandlerAdapter {
    private byte type;
    private byte flag;
    private int length;
    private String body;
    private String encoding = "UTF-8";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(String.format("channelActive %s", ctx.channel().remoteAddress()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        type = buf.readByte();
        flag = buf.readByte();
        length = buf.readInt();

        int len = buf.readableBytes();
        byte[] req = new byte[len];
        buf.readBytes(req);
        body = new String(req, encoding);

        CustomMsg entityMessage = new CustomMsg(type, flag, length, body);

        System.out.println(String.format("ip:%s %s", ctx.channel().remoteAddress(), entityMessage));
    }
}
