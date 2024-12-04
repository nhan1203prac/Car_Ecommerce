package com.example.cartandorder.ReponseApi;

import java.util.List;

public class CarCreateResponse {
    private String brand;
    private String name;
    private String description;
    private double price;
    private List<String> images;
    private long carId;
    private List<Comment> comments;
    private User user;

    public CarCreateResponse(String brand, String name, String description, double price, List<String> pictures, long carId,List<Comment> comment,User user) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.price = price;
        this.images = pictures;
        this.carId = carId;
        this.comments = comment;
        this.user = user;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comment) {
        this.comments = comment;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getImages() {
        return images;
    }

    public long getCarId() {
        return carId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImages(List<String> pictures) {
        this.images = pictures;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
