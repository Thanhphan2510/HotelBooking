package com.example.hotelbooking.Item;

import java.util.ArrayList;

public class Room {
    private String name;
    private int price;
    private String description;
    private ArrayList<String> facilites;

    public Room(String name, int price, String description, ArrayList<String> facilites) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.facilites = facilites;
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

    public ArrayList<String> getFacilites() {
        return facilites;
    }

    public void setFacilites(ArrayList<String> facilites) {
        this.facilites = facilites;
    }
}
