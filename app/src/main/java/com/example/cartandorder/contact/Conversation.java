package com.example.cartandorder.contact;

import java.util.List;

public class Conversation {
    private String _id;
    private List<Long> participants;
    private String createdAt;
    private String updatedAt;

    public Conversation() {}

    public Conversation(String _id, List<Long> participants, String createdAt, String updatedAt) {
        this._id = _id;
        this.participants = participants;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}