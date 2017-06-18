package com.mash.server;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Mash on 15/05/2016.
 */
public class PacketFactory {

    static Packet LoginResult(boolean success, String sessionId, String message){
        Packet p = new Packet(Packet.OpCode.LoginResult);
        p.setBool("status", success);
        p.setString("userId", sessionId);
        p.setString("message", message);
        return p;
    }

    static Packet Error(String message){
        Packet p = new Packet(Packet.OpCode.Error);
        p.setString("message", message);
        return p;
    }

    static Packet RoomFound(int roomId, String roomName, ArrayList<Client> clients){
        Packet p = new Packet(Packet.OpCode.RoomFound);
        p.setInt("roomId", roomId);
        p.setString("roomName", roomName);

        ArrayList<JSONObject> clientInfo = new ArrayList<>();
        for(Client c : clients){
            JSONObject obj = new JSONObject();
            obj.put("userId", c.getId());
            obj.put("firstName", c.getFirstName());
            obj.put("lastName", c.getLastName());
            obj.put("pictureUrl", c.getPictureUrl());
            clientInfo.add(obj);
        }
        p.getPayload().put("clients", clientInfo);
        return p;
    }

    static Packet ClientLeft(Client c) {
        Packet p = new Packet(Packet.OpCode.ClientLeft);
        p.setString("userId", c.getId());
        return p;
    }
}
