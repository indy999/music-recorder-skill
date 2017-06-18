package com.mash.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by matthew.casey on 11/05/2016.
 */
public class CustomChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final int MAX_FRAME_LENGTH = 8092;
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int LENGTH_FIELD_LENGTH= 4;
    private static final int LENGTH_FIELD_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 4;

    private ClientManager clientManager;
    private RoomManager roomManager;

    public CustomChannelInitializer(ClientManager clientManager, RoomManager roomManager){
        this.clientManager = clientManager;
        this.roomManager = roomManager;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("Initializing new channel...");

        //Inbound
        socketChannel.pipeline().addLast("inboundFramer", new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_FIELD_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
        socketChannel.pipeline().addLast("decoder", new PacketDecoder());
        socketChannel.pipeline().addLast("handler", new PacketHandler(clientManager, roomManager));

        //Outbound
        socketChannel.pipeline().addLast("outboundFramer", new LengthFieldPrepender(LENGTH_FIELD_LENGTH, LENGTH_FIELD_ADJUSTMENT));
        socketChannel.pipeline().addLast("encoder", new PacketEncoder());

    }
}
