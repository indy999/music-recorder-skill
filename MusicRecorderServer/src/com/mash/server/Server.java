package com.mash.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
/**
 * Created by matthew.casey on 11/05/2016.
 */
public class Server {

    private final int port;
    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;
    private ClientManager clientManager = new ClientManager();
    private RoomManager roomManager = new RoomManager();

    Server(int port){
        this.port = port;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
    }

    void run(){
        System.out.println("Starting server on port..." + port);
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new CustomChannelInitializer(clientManager, roomManager));
        ChannelFuture f = bootstrap.bind(this.port);
        f.addListener(
                new GenericFutureListener<Future<Void>>() {
                    @Override
                    public void operationComplete(io.netty.util.concurrent.Future<Void> voidFuture) throws Exception {
                        System.out.println("Success: Server listening on port " + port);
                    }
                }
        );
    }

    public static void main(String[] args) {
        int port = 8080;
        new Server(port).run();
    }
}
