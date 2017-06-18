package com.mash.server;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import io.netty.buffer.ByteBuf;
import org.json.JSONObject;

/**
 * Created by matthew.casey on 11/05/2016.
 */
public class Packet {

    public enum OpCode {
        Invalid(-1),
        Echo(0),
        Login(1),
        LoginResult(2),
        FindRoom(3),
        RoomFound(4),
        ClientLeft(5),
        Broadcast(6),
        Error(7),
        Notify(8);

        private final int value;

        OpCode(int value) {
            this.value = value;
        }

        public int toInt() {
            return value;
        }
        public static OpCode fromInt(int i) {
            for(OpCode code : OpCode.values()) {
                if(code.toInt() == i)
                    return code;
            }
            return Invalid;
        }
    }

    private OpCode opCode;
    private JSONObject payload;

    public Packet(OpCode opCode) {
        this.opCode = opCode;
        this.payload = new JSONObject();
    }
    public Packet(OpCode opCode, JSONObject payload){
        this.opCode = opCode;
        this.payload = payload;
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public void setOpCode(OpCode opCode) {
        this.opCode = opCode;
    }

    public JSONObject getPayload() {
        return payload;
    }

    public void setPayload(JSONObject payload) {
        this.payload = payload;
    }

    public void setInt(String name, int value) {
        this.payload.put(name, value);
    }

    public void setFloat(String name, float value) {
        this.payload.put(name, value);
    }

    public void setBool(String name, boolean value) {
        this.payload.put(name, value);
    }

    public void setString(String name, String value) {
        this.payload.put(name, value);
    }

    public int getInt(String name) {
        return payload.getInt(name);
    }

    public float getFloat(String name) {
        return payload.getInt(name);
    }

    public boolean getBool(String name) {
        return payload.getBoolean(name);
    }

    public String getString(String name) {
        return payload.getString(name);
    }
}
