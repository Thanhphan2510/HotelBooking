package com.example.hotelbooking.Item;

import java.io.Serializable;

public class HomeItem implements Serializable {
    private int imageView;
    private String name;
    private float star;
    private int price; // for 1 night
    private String description1;
    private String description2;
    private String description3;

    public HomeItem(int imageView, String name, float star, int price, String description1, String description2, String description3) {
        this.imageView = imageView;
        this.name = name;
        this.star = star;
        this.price = price;
        this.description1 = description1;
        this.description2 = description2;
        this.description3 = description3;
    }

    public float getRating() {
        return star;
    }

    public void setRating(float rating) {
        this.star = rating;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }
}
