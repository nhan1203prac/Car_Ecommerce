package com.example.cartandorder.ReponseApi;

import java.util.ArrayList;
import java.util.List;

public class CarResponse {
    private String brand;
    private String name;
    private String description;
    private double price;
    private List<Images> images= new ArrayList<>();
    private long carId;
    private List<Comment> comments = new ArrayList<>();
    private User user;
    private String color;

    public CarResponse(String brand, String name, String description, double price, List<Images> images, long carId, List<Comment> comments, User user,String color) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.price = price;
        this.images = images;
        this.carId = carId;
        this.comments = comments;
        this.user = user;
        this.color = color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
