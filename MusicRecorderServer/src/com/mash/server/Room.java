package com.mash.server;

import java.util.ArrayList;

/**
 * Created by Mash on 15/05/2016.
 */
public class Room {
    private final String name;
    private final int id;
    private int maxPlayers;
    private boolean available = true;
    private ArrayList<Client> clients = new ArrayList<>();

    public Room(int id, String name, int maxPlayers){
        this.id = id;
        this.name = name;
        this.maxPlayers = maxPlayers;
    }

    public synchronized void Add(Client c){
        if(clients.size() < maxPlayers)
            clients.add(c);
    }

    public synchronized void Remove(Client c){
        clients.remove(c);
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isFull() {
        return clients.size() >= maxPlayers;
    }

    public boolean isEmpty() {
        return clients.size() == 0;
    }

    public synchronized void broadcastToAll(Packet p) {
        for(Client c : clients)
            c.sendPacket(p);
    }

    public synchronized void broadcastToOthers(Packet p, Client exceptClient){
        for(Client c : clients){
            if(c.getId() != exceptClient.getId()){
                c.sendPacket(p);
            }
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }
}
