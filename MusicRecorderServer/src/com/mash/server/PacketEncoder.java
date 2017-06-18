package com.mash.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by matthew.casey on 12/05/2016.
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        System.out.println("Encoding packet " + packet.getOpCode().toString() + " with payload " + packet.getPayload().toString());
        byteBuf.writeInt(packet.getOpCode().toInt());
        byteBuf.writeBytes(packet.getPayload().toString().getBytes());
    }
}
