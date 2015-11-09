package com.wds.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by wds on 2015/10/3.
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    private static final Logger LOGGER = LogManager.getLogger(TimeServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);

        String body = new String(req, CharsetUtil.UTF_8);

        LOGGER.info("The time server receive order :" + body);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";

        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        ctx.write(resp);


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage(), cause);
        ctx.close();
    }
}
