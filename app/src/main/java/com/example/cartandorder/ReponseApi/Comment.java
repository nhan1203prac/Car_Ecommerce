package com.example.cartandorder.ReponseApi;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.cartandorder.favourite.Car;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Comment implements Parcelable {
    private Long commentId;

    private User user;

    private Car car;
    private String content;
    private double rating=0;
    private Long likes = 0L;
    private Long dislike = 0L;


    private Set<Long> likedByUsers = new HashSet<>();


    private Set<Long> dislikedByUsers = new HashSet<>();

    private LocalDate createdAt;

    public Comment(Long commentId, User user, Car car, String content, double rating, Long likes, Long dislike, Set<Long> likedByUsers, Set<Long> dislikedByUsers, LocalDate createdAt) {
        this.commentId = commentId;
        this.user = user;
        this.car = car;
        this.content = content;
        this.rating = rating;
        this.likes = likes;
        this.dislike = dislike;
        this.likedByUsers = likedByUsers;
        this.dislikedByUsers = dislikedByUsers;
        this.createdAt = createdAt;
    }

    public Long getCommentId() {
        return commentId;
    }

    public User getUser() {
        return user;
    }

    public Car getCar() {
        return car;
    }

    public String getContent() {
        return content;
    }

    public double getRating() {
        return rating;
    }

    public long getLikes() {
        return likes;
    }

    public long getDislike() {
        return dislike;
    }

    public Set<Long> getLikedByUsers() {
        return likedByUsers;
    }

    public Set<Long> getDislikedByUsers() {
        return dislikedByUsers;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public void setDislike(Long dislike) {
        this.dislike = dislike;
    }

    public void setLikedByUsers(Set<Long> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }

    public void setDislikedByUsers(Set<Long> dislikedByUsers) {
        this.dislikedByUsers = dislikedByUsers;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    protected Comment(Parcel in) {
        commentId = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        car = in.readParcelable(Car.class.getClassLoader());
        content = in.readString();
        rating = in.readDouble();
        likes = in.readLong();
        dislike = in.readLong();
        // Chuyển đổi long[] thành HashSet<Long> đúng cách
        long[] longArrayLiked = in.createLongArray();
        likedByUsers = new HashSet<>();
        for (long id : longArrayLiked) {
            likedByUsers.add(id);
        }

        long[] longArrayDisliked = in.createLongArray();
        dislikedByUsers = new HashSet<>();
        for (long id : longArrayDisliked) {
            dislikedByUsers.add(id);
        }

        createdAt = (LocalDate) in.readSerializable();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(commentId);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(car, flags);
        dest.writeString(content);
        dest.writeDouble(rating);
        dest.writeLong(likes);
        dest.writeLong(dislike);
        dest.writeLongArray(likedByUsers.stream().mapToLong(Long::longValue).toArray());
        dest.writeLongArray(dislikedByUsers.stream().mapToLong(Long::longValue).toArray());
        dest.writeSerializable(createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
