package com.example.cartandorder.notification;

import com.example.cartandorder.ReponseApi.User;

import java.time.LocalDate;

public class Notification {
    private Long notifiId;

    private User user;
    private String title;
    private String message;


    private LocalDate createdAt;

    public Notification(Long notifiId, User user, String title, String message, LocalDate createdAt) {
        this.notifiId = notifiId;
        this.user = user;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Notification(){

    }

    public Long getNotifiId() {
        return notifiId;
    }

    public void setNotifiId(Long notifiId) {
        this.notifiId = notifiId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
