package com.wds.netty.encoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by wds on 2015/10/7.
 */
public class TestUserInfo {

    private static final Logger LOGGER = LogManager.getLogger(TestUserInfo.class);

    public static void main(String[] args) {
        UserInfo info = new UserInfo();
        info.buildUserID(100).buildUserName("Welcome to netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(info);
            oos.flush();

            byte[] bytes = bos.toByteArray();
            LOGGER.info("The JDK serializable length is :" + bytes.length);

            LOGGER.info("-------美丽的分隔线-------");
            LOGGER.info("The byte array serializable length is :" + info.codeC().length);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                bos.close();
                bos.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

}
