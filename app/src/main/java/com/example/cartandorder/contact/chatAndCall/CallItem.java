package com.example.cartandorder.contact.chatAndCall;

import com.example.cartandorder.ReponseApi.User;

import java.time.LocalDateTime;

public class CallItem {

    private Long callId;
    private User sender;
    private User receiver;
    private LocalDateTime time;


    public CallItem(Long callId, User sender, User receiver, LocalDateTime time) {
        this.callId = callId;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
