package com.mash.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.json.JSONObject;

import java.nio.charset.Charset;


/**
 * Created by matthew.casey on 11/05/2016.
 */
public class PacketDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        try{
            assert buf.readableBytes() >= 4;
            int opCode = buf.readInt();
            String p = buf.toString(Charset.defaultCharset());
            JSONObject payload = new JSONObject(p);
            ctx.fireChannelRead(new Packet(Packet.OpCode.fromInt(opCode), payload));
        }
        finally {
            buf.release();
        }
    }
}
