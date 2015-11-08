package com.wds.netty.selfprotocol;

import com.wds.netty.marshalling.MarshallingCodeCfactory;
import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.Marshalling;

import java.io.IOException;

/**
 * Created by wds on 2015/11/2.
 */
public class MarshallingEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    private Marshaller marshaller;

    public MarshallingEncoder() throws IOException {
        marshaller = MarshallingCodecFactory.buildMarshalling();
    }

    protected void encode(Object msg, ByteBuf out) throws Exception {
        try {
            int lengthPos = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);

            marshaller.start(output);
            marshaller.writeObject(msg);
            marshaller.finish();

            out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
        } finally {
            marshaller.close();
        }

    }
}