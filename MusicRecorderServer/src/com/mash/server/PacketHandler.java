package com.mash.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

/**
 * Created by matthew.casey on 11/05/2016.
 */
public class PacketHandler extends ChannelInboundHandlerAdapter {

    private final ClientManager clientManager;
    private final RoomManager roomManager;

    Channel channel = null;
    Client client = null;
    Room room = null;

    public PacketHandler(ClientManager clientManager, RoomManager roomManager) {
        this.clientManager = clientManager;
        this.roomManager = roomManager;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel registered:" + ctx.channel().remoteAddress().toString());
        channel = ctx.channel();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if(client == null){
            return;
        }

        if(room == null) {
            client = null;
            return;
        }

        System.out.println("Channel unregistered:" + ctx.channel().remoteAddress().toString());
        clientManager.deleteClient(ctx.channel());
        room.Remove(client);

        if(!room.isEmpty()) {
            room.broadcastToAll(PacketFactory.ClientLeft(client));
        } else {
            roomManager.deleteRoom(room);
            System.out.println("Deleted room:" + room.getName());
            System.out.println("Total rooms:" + roomManager.getNumRooms());
            System.out.println("Total clients:" + clientManager.getNumClients());
        }
        client = null;
        room = null;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        assert(client != null);

        Packet packet = (Packet)msg;
        System.out.println("Handling packet " + packet.getOpCode().toString());

        switch(packet.getOpCode()){
            case Echo:
                ctx.pipeline().write(packet);
                break;
            case Login:
                Packet loginResult = authenticate(packet);
                ctx.channel().pipeline().writeAndFlush(loginResult);
                break;
            case FindRoom:
                assert(client != null);
                assert(room == null);

                String roomName = packet.getPayload().getString("name");
                int maxPlayers = packet.getPayload().getInt("maxPlayers");
                room = roomManager.findRoom(roomName, maxPlayers);
                if(!room.isFull())
                {
                    room.Add(client);
                    if(room.isFull()){
                        room.setAvailable(false);
                        room.broadcastToAll(PacketFactory.RoomFound(room.getId(), room.getName(), room.getClients()));
                    }
                }
                break;
            case Broadcast:{
                assert(client != null);
                assert(room != null);
                room.broadcastToOthers(packet, client);
                break;
            }
            case Notify:{
                List<Client> clients = clientManager.getClients();
                for (Client c : clients) {
                    c.sendPacket(packet);
                }
                break;
            }

            default:
                System.out.println("WARNING: Unhandled packet " + packet.getOpCode().toString() + ": " + packet.getPayload().toString());
                break;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.pipeline().writeAndFlush(PacketFactory.Error(cause.toString()));
        ctx.close();
    }

    private Packet authenticate(Packet packet){
        assert(client == null);

        String id = packet.getPayload().getString("userId");
        String firstName = packet.getPayload().getString("firstName");
        String lastName = packet.getPayload().getString("lastName");
        String pictureUrl = packet.getPayload().getString("pictureUrl");

        try
        {
            Integer.parseInt(id);
            client = clientManager.createClient(channel);
            client.setId(id);
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setPictureUrl(pictureUrl);
            return PacketFactory.LoginResult(true, client.getId(), "");
        }
        catch (Exception e) {
            return PacketFactory.LoginResult(false, "", "Bad user id");
        }
    }
}
