package com.wds.netty.marshalling;

import com.wds.netty.protobuf.SubreqclientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.PrimitiveIterator;
import java.util.Properties;

/**
 * Created by wds on 2015/10/16.
 */
public class SubReqClient {

    private static final Logger LOGGER = LogManager.getLogger(SubReqClient.class);

    public static void main(String[] args) {
        int port = 8080;

        new SubReqClient().connect(port);
    }

    private void connect(int port) {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(MarshallingCodeCfactory.buildMarshallingDecoder());
                ch.pipeline().addLast(MarshallingCodeCfactory.buildMarshallingEncoder());
                ch.pipeline().addLast(new SubreqclientHandler());
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
