package com.wds.netty.selfprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.jboss.marshalling.Marshalling;

import java.util.List;
import java.util.Map;

/**
 * Created by wds on 2015/11/2.
 */
public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {
    private MarshallingEncoder marshallingEncoder;



    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }

//        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionId());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeInt(msg.getHeader().getAttchment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : msg.getHeader().getAttchment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes(CharsetUtil.UTF_8);
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value, sendBuf);
        }
        key = null;
        keyArray = null;
        value = null;
        if (msg.getBoyd() != null) {
            marshallingEncoder.encode(msg.getBoyd(), sendBuf);
        } else {
            sendBuf.writeInt(0);
        }
        sendBuf.setIndex(4, sendBuf.readableBytes());
    }
}
