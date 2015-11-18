package com.wds.netty.delimiter.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by wds on 2015/10/7.
 */
public class EchoServer {
    private static final Logger LOGGER = LogManager.getLogger(EchoServer.class);

    public static void main(String[] args) {
        new EchoServer().bind(8080);
    }

    private void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.TRACE)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new EchoServerHandler());
            }
        });

        ChannelFuture future = null;
        try {
            future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                bossGroup.close();
                workerGroup.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}
