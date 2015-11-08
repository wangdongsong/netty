package com.wds.netty.messagepack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by wds on 2015/10/8.
 */
public class EchoClient {
    private final static Logger LOGGER = LogManager.getLogger(EchoClient.class);

    private final String host;
    private final int port;
    private final int sendNumber;

    public static void main(String[] args) {
        try {
            new EchoClient("127.0.0.1", 8080, 10).run();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public EchoClient(String host, int port, int sendNumber) {
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(50000, 0, 2, 0, 2));
                ch.pipeline().addLast("messagePack decoder", new MsgPackDecoder());
                ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                ch.pipeline().addLast("messagePack encoder", new MsgPackEncoder());
                ch.pipeline().addLast(new EchoClientHandler(sendNumber));
            }
        });

        try {
            ChannelFuture future = bootstrap.connect(this.host, this.port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
