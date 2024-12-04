package com.example.cartandorder.favourite;

import android.os.Parcel;
import android.os.Parcelable;

public class Car implements Parcelable {
    private String name;
    private String color;
    private String price;
    private String desc;
    private String brand;
    private String url;
    private Long carId;

    // Constructor
    public Car(String name, String color, String price, String desc, String brand, String url, Long carId) {
        this.name = name;
        this.color = color;
        this.price = price;
        this.desc = desc;
        this.brand = brand;
        this.url = url;
        this.carId = carId;
    }

    // Getter and Setter methods
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Implement Parcelable
    protected Car(Parcel in) {
        name = in.readString();
        color = in.readString();
        price = in.readString();
        desc = in.readString();
        brand = in.readString();
        url = in.readString();
        if (in.readByte() == 0) {
            carId = null;
        } else {
            carId = in.readLong();
        }
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(color);
        dest.writeString(price);
        dest.writeString(desc);
        dest.writeString(brand);
        dest.writeString(url);
        if (carId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(carId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
