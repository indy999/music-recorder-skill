package com.mash.server;

import io.netty.channel.Channel;

/**
 * Created by matthew.casey on 12/05/2016.
 */
public class Client {
    private Channel channel;
    private String userId;
    private String firstName;
    private String lastName;
    private String pictureUrl;

    public Client(Channel channel) {
        this.channel = channel;
        this.userId = "";
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getId(){
        return userId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPictureUrl(){
        return pictureUrl;
    }

    public Channel getChannel() {
        return channel;
    }

    void sendPacket(Packet p) {
        channel.pipeline().writeAndFlush(p);
    }
}
