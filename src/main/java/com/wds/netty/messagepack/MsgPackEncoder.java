package com.wds.netty.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by wds on 2015/10/8.
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {
    private MessagePack msgPack = new MessagePack();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] raw = msgPack.write(msg);
        out.writeBytes(raw);
    }
}
