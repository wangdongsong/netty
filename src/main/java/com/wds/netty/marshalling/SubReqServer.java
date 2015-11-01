package com.wds.netty.marshalling;

import com.wds.netty.protobuf.SubReqServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by wds on 2015/10/15.
 */
public class SubReqServer {
    private final static Logger LOOGER = LogManager.getLogger(SubReqServer.class);

    public static void main(String[] args) {
        new SubReqServer().bind(8080);
    }

    public void bind(int port) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.TRACE)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(MarshallingCodeCfactory.buildMarshallingDecoder());
                ch.pipeline().addLast(MarshallingCodeCfactory.buildMarshallingEncoder());
                ch.pipeline().addLast(new SubReqServerHandler());
            }
        });

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOOGER.error(e.getMessage(), e);

        }
    }

}
