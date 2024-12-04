package com.example.cartandorder.RequestApi;

import java.util.List;

public class CarCreateRequest {
    private String brand;
    private String name;
    private String description;
    private double price;
    private List<String> pictures;

    public CarCreateRequest(String brand, String name, String description,double price, List<String> pictures) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.price = price;
        this.pictures = pictures;
    }

    public String getDescription() {
        return description;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
