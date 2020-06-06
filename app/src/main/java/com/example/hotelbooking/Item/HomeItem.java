package com.example.hotelbooking.Item;

import java.io.Serializable;

public class HomeItem implements Serializable {
    private String id;
    private String imageView;
    private String name;
    private float star;
    private int price; // for 1 night
    private String description1;
    private String description2;
    private String description3;

    public HomeItem(String id,String imageView, String name, float star, int price, String description1, String description2, String description3) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
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

    @Override
    public String toString() {
        return "HomeItem{" +
                "imageView=" + imageView +
                ", name='" + name + '\'' +
                ", star=" + star +
                ", price=" + price +
                ", description1='" + description1 + '\'' +
                ", description2='" + description2 + '\'' +
                ", description3='" + description3 + '\'' +
                '}';
    }
}
