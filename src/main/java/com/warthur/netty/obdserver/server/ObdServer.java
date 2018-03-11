package com.warthur.netty.obdserver.server;

import com.warthur.netty.obdserver.codec.HexMessageDecoder;
import com.warthur.netty.obdserver.codec.ObdMessageEncoder;
import com.warthur.netty.obdserver.common.util.Constants;
import com.warthur.netty.obdserver.handler.ObdServerHandler;
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
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObdServer {

    private final String host;
    private final int port;

    public ObdServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap sb = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer(Constants.MSG_DELIMITER.getBytes());
                            ch.pipeline()
                                    .addLast("decoder", new HexMessageDecoder())
                                    .addLast("encoder", new ObdMessageEncoder())
                                    .addLast(new IdleStateHandler(0, 0, 300))
                                    .addLast("handler", new ObdServerHandler());
                        }
                    });
            ChannelFuture future = sb.bind(host, port).sync();
            log.info("{} started and listen on {}", ObdServer.class.getSimpleName(), future.channel().localAddress());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("netty server start fail! ", e);
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }
}
