package com.wds.netty.selfprotocol;

import com.wds.netty.basic.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wds on 2015/11/4.
 */
public class NettyClient {
    private static final Logger LOGGER = LogManager.getLogger(NettyClient.class);
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private EventLoopGroup group = new NioEventLoopGroup();

    public static void main(String[] args) {
        try {
            new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void connect(int port, String host) throws Exception {
        try {


            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new TimeClientHandler());
                    ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                    ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                    //ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                    ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
                    //ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                }
            });

            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            future.channel().closeFuture().sync();
        }finally {
//            executor.execute(() -> {
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                    //重新连接
//                    connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
//                } catch (InterruptedException e) {
//                    LOGGER.error(e.getMessage(), e);
//                } catch (Exception e) {
//                    LOGGER.error(e.getMessage(), e);
//                }
//
//            });
        }

    }
}
