package com.mash.server;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matthew.casey on 12/05/2016.
 */
public class ClientManager {

    private Map<Channel, Client> clients = new HashMap<>();

    public synchronized Client createClient(Channel channel){
        Client client = new Client(channel);
        clients.put(channel, client);
        return client;
    }

    public synchronized void deleteClient(Channel channel){
        clients.remove(channel);
    }

    public synchronized Client getClient(Channel channel){
        if(clients.containsKey(channel))
            return clients.get(channel);
        return null;
    }

    public synchronized int getNumClients() {
        return clients.size();
    }

    public List<Client> getClients(){
        List<Client> clientList = new ArrayList<>();
        for (Map.Entry<Channel, Client> entry : clients.entrySet()) {
            clientList.add(entry.getValue());
        }
        return clientList;
    }
}
