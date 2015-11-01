package com.wds.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by wds on 2015/10/31.
 */
public class WebSocketServer {

    private static final Logger LOGGER = LogManager.getLogger(WebSocketServer.class);

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new WebSocketServer().run(port);
    }

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("http-codec", new HttpServerCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(655636));
                pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                pipeline.addLast("handler", new WebsocketServerHandler());
            }
        });

        Channel ch = bootstrap.bind(port).sync().channel();
        ch.closeFuture().sync();
        LOGGER.info("Web socket server started at port", + port);
        LOGGER.info("Open your brower and navigate to http://localhost:" + port + "/");

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
