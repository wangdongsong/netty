package com.wds.netty.messagepack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import sun.plugin2.message.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wds on 2015/10/7.
 */
public class MessagePackTest {
    private static final Logger LOGGER = LogManager.getLogger(MessagePackTest.class);

    public static void main(String[] args) throws IOException {
        List<String> src = new ArrayList<>();
        src.add("messagePackage");
        src.add("wds");
        src.add("HelloWorld");

        MessagePack msgPack = new MessagePack();
        byte[] bytes = msgPack.write(src);

        List<String> dst = msgPack.read(bytes, Templates.tList(Templates.TString));
        LOGGER.info(dst);

    }
}
