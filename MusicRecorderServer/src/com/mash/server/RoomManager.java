package com.mash.server;

import java.util.ArrayList;

/**
 * Created by Mash on 15/05/2016.
 */
public class RoomManager {

    private ArrayList<Room> rooms = new ArrayList<Room>();
    private static int roomId = 0;


    public synchronized Room findRoom(String name, int maxPlayers) {
        for (Room room : rooms) {
            if (room.isAvailable()) {
                if (room.getName().equals(name)) {
                    return room;
                }
            }
        }

        Room newRoom = new Room(roomId++, name, maxPlayers);
        rooms.add(newRoom);
        return newRoom;
    }

    public synchronized void deleteRoom(Room room){
        rooms.remove(room);
    }

    public synchronized int getNumRooms() {
        return rooms.size();
    }
}
