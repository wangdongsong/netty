package com.wds.netty.delimiter.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by wds on 2015/10/7.
 */
public class EchoClient {
    private static final Logger LOGGER = LogManager.getLogger(EchoClient.class);

    public static void main(String[] args) {
        new EchoClient().bind(8080);
    }

    private void bind(int port) {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new EchoClientHandler());
            }
        });

        try {
            ChannelFuture future = bootstrap.connect("127.0.0.1", port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            group.shutdownGracefully();
        }


    }
}
