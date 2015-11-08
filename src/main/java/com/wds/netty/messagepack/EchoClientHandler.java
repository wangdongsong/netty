package com.wds.netty.messagepack;

import com.wds.netty.encoder.UserInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by wds on 2015/10/8.
 */
public class EchoClientHandler extends ChannelHandlerAdapter {
    private final static Logger LOGGER = LogManager.getLogger(EchoClientHandler.class);
    private final int sendNumber;

    public EchoClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] infos = UserInfos();
        for (UserInfo info : infos) {
            LOGGER.info("ctx.write:" + info.toString());

            ctx.write(info);
        }

        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("Client receive the msgpackage message : " + msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage(), cause);
    }



    private UserInfo[] UserInfos() {
        UserInfo[] userInfos = new UserInfo[sendNumber];
        UserInfo userInfo = null;
        for (int i = 0; i < sendNumber; i++) {
            userInfo = new UserInfo();
            userInfo.setUserID(i);
            userInfo.setUserName("ABCDEFG---->" + i);
            userInfos[i] = userInfo;
        }

        return userInfos;
    }
}
