package com.example.hotelbooking.Item;

public class HomeItem {
    private int imageView;
    private String name;
    private float price; // for 1 night
    private String description;

    public HomeItem(int imageView, String name, float price, String description) {
        this.imageView = imageView;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
