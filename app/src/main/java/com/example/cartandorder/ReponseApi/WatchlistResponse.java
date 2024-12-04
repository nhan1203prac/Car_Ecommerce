package com.example.cartandorder.ReponseApi;

import java.util.ArrayList;
import java.util.List;

public class WatchlistResponse {
    private long favouriteId;
    private User user;
    private List<CarWatchlistResponse> cars = new ArrayList<>();

    public WatchlistResponse(long favouriteId, User user, List<CarWatchlistResponse> cars) {
        this.favouriteId = favouriteId;
        this.user = user;
        this.cars = cars;
    }

    public long getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(long favouriteId) {
        this.favouriteId = favouriteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CarWatchlistResponse> getCars() {
        return cars;
    }

    public void setCars(List<CarWatchlistResponse> cars) {
        this.cars = cars;
    }
}
