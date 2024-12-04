package com.example.cartandorder.ReponseApi;

public class LoginResponse {
    private String jwt;
    private String message;
    private boolean status;
    private Long userId;



    public LoginResponse(String jwt, String message, boolean status, Long userId) {
        this.jwt = jwt;
        this.message = message;
        this.status = status;
        this.userId = userId;

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getJwt() {
        return jwt;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
