package com.example.hotelbooking.Item;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private String name;
    private int price;
    private String description;
    @PropertyName("facilites")
    private List<String> facilites;

    public Room(String name, int price, String description, List<String> facilites) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.facilites = facilites;
    }

    public Room() {
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", facilites=" + facilites +
                '}';
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
    @PropertyName("facilites")
    public List<String> getFacilites() {
        return facilites;
    }
    @PropertyName("facilites")
    public void setFacilites(List<String> facilites) {
        this.facilites = facilites;
    }
}
