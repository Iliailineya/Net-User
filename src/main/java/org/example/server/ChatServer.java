package org.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.*;
import org.example.util.LogConfig;

import java.util.logging.Logger;

import static org.example.util.Constants.*;

public final class ChatServer {

    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());

    public static void main(String[] args) throws Exception {
        LogConfig.configureLogging("server");

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new ChatServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(PORT).sync();
            System.out.println(SERVER_START_MSG);
            logger.info(SERVER_START_MSG);

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
