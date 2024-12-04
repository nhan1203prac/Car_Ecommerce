package com.example.cartandorder.searchBar;

public class Car {
    private String name;
    private String color;
    private String price;
    private String desc;
    private String brand;

    public Car(String name, String color, String price,String desc,String brand) {
        this.name = name;
        this.color = color;
        this.price = price;
        this.desc = desc;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }



    public String getPrice() {
        return price;
    }
    public String getDesc() {
        return desc;
    }

    public String getBrand() {
        return brand;
    }
}
