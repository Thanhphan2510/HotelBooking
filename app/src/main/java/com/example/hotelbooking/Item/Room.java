package com.example.hotelbooking.Item;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.List;

public class Room implements Serializable {
    private String roomID, name;
    private int price;
    private String description;
    @PropertyName("facilities")
    private List<String> facilities;

    public Room(String roomID, String name, int price, String description, List<String> facilities) {
        this.roomID = roomID;
        this.name = name;
        this.price = price;
        this.description = description;
        this.facilities = facilities;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID='" + roomID + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", facilites=" + facilities +
                '}';
    }

    public Room() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @PropertyName("facilities")
    public List<String> getFacilities() {
        return facilities;
    }
    @PropertyName("facilities")
    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }
}
