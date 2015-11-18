package com.wds.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.jndi.cosnaming.IiopUrl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wds on 2015/10/13.
 */
public class TestSubscribeReqProto {
    private static final Logger LOGGER = LogManager.getLogger(TestSubscribeReqProto.class);

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        LOGGER.info("Before encode : " + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        LOGGER.info("After decode :" + req.toString());
        LOGGER.info("Assert equal :" + req2.equals(req));

    }

    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();

        builder.setSubReqId(1);
        builder.setUserName("Lilinfeng");
        builder.setProductName("Netty Book");
        List<String> address = new ArrayList<>();
        address.add("NanJing");
        address.add("BeiJing");
        address.add("ShenZhen");
        builder.addAllAddress(address);

        return builder.build();
    }

}
