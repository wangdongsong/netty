package com.wds.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by wds on 2015/10/3.
 */
public class TimeServer {

    private static final Logger LOGGER = LogManager.getLogger(TimeServer.class);

    public static void main(String[] args) {
        int port = 8080;
        if (ArrayUtils.isNotEmpty(args)) {
            port = Integer.valueOf(args[0]);
        }
        new TimeServer().bind(port);
    }

    public void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler());

        //绑定端口，同步等待成功
        ChannelFuture f = null;
        try {
            f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            //释放资源
            try {
                bossGroup.close();
                workerGroup.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }
}
