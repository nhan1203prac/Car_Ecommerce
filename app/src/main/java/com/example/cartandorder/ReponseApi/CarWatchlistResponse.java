package com.example.cartandorder.ReponseApi;

import android.os.Parcel;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarWatchlistResponse implements  Parcelable {
    private String brand;
    private String name;
    private String description;
    private double price;
    private List<Images> images= new ArrayList<>();
    private long carId;
    private List<Comment> comments = new ArrayList<>();
    private User user;
    private String color;
    public CarWatchlistResponse(String brand, String name, String description, double price, List<Images> images, long carId, List<Comment> comments, User user,String color) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    protected CarWatchlistResponse(Parcel in) {
        brand = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        images = in.createTypedArrayList(Images.CREATOR);
        carId = in.readLong();
        comments = in.createTypedArrayList(Comment.CREATOR);
        user = in.readParcelable(User.class.getClassLoader());
        color = in.readString();
    }

    public static final Parcelable.Creator<CarWatchlistResponse> CREATOR = new Parcelable.Creator<CarWatchlistResponse>() {
        @Override
        public CarWatchlistResponse createFromParcel(Parcel in) {
            return new CarWatchlistResponse(in);
        }

        @Override
        public CarWatchlistResponse[] newArray(int size) {
            return new CarWatchlistResponse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeTypedList(images);
        dest.writeLong(carId);
        dest.writeTypedList(comments);
        dest.writeParcelable(user, flags);
        dest.writeString(color);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
