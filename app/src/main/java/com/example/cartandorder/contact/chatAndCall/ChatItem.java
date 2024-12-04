package com.example.cartandorder.contact.chatAndCall;

public class ChatItem {
    private String name;
    private String message;
    private String image;
    private Long userId2;
    public ChatItem(String name, String message, String image,Long userId2) {
        this.name = name;
        this.message = message;
        this.image = image;
        this.userId2 = userId2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getUserId2() {
        return userId2;
    }

    public void setUserId2(Long userId2) {
        this.userId2 = userId2;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }
}
