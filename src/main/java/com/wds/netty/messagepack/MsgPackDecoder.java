package com.wds.netty.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by wds on 2015/10/8.
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

    private MessagePack msgPack = new MessagePack();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final int length = msg.readableBytes();
        final byte[] array = new byte[length];

        msg.getBytes(msg.readerIndex(), array, 0, length);
        out.add(msgPack.read(array));
    }
}
