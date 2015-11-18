package com.wds.netty.encoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by wds on 2015/10/7.
 */
public class PerformTestuserInfo {
    private static final Logger LOGGER = LogManager.getLogger(PerformTestuserInfo.class);

    public static void main(String[] args) throws IOException{
        UserInfo info = new UserInfo();
        info.buildUserID(100).buildUserName("Welcome to Netty");
        int loop = 100000;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(info);
            oos.flush();
            byte[] b = bos.toByteArray();
            bos.close();
        }

        long endTime = System.currentTimeMillis();

        LOGGER.info("The jdk serializable cost time is :" + (endTime - startTime) + "ms");

        LOGGER.info("-------美丽的分隔线-------");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            byte[] bytes = info.codeC();
        }
        endTime = System.currentTimeMillis();
        LOGGER.info("The byte array serializable cost time is :" + (endTime - startTime) + "ms");

    }
}
