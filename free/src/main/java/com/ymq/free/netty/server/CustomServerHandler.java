package com.ymq.free.netty.server;

import com.ymq.free.netty.protocol.CustomMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CustomServerHandler extends SimpleChannelInboundHandler<Object> {

    private byte type;
    private byte flag;
    private int length;
    private String body;
    private String encoding = "UTF-8";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

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

        entityMessage.setBody(String.format("server:%s orogin:%s", System.currentTimeMillis(), entityMessage.getBody()));

        ctx.channel().writeAndFlush(entityMessage);

        /*if (msg instanceof CustomMsg) {
            CustomMsg customMsg = (CustomMsg) msg;
            System.out.println(String.format("client:%s type:%s flag:%s len:%s data:%s",
                    ctx.channel().remoteAddress(),
                    customMsg.getType(),
                    customMsg.getFlag(),
                    customMsg.getLength(),
                    customMsg.getBody()));
        } else {
            System.out.println(String.format("无效消息:%s", msg));
        }*/
    }
}
