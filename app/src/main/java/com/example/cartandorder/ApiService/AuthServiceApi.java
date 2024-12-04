package com.example.cartandorder.ApiService;

import com.example.cartandorder.ReponseApi.LoginResponse;
import com.example.cartandorder.ReponseApi.ResetPasswordResponse;
import com.example.cartandorder.ReponseApi.User;
import com.example.cartandorder.RequestApi.LoginRequest;
import com.example.cartandorder.RequestApi.RegisterRequest;
//import com.example.cartandorder.RequestApi.ResetPassword;
import com.example.cartandorder.RequestApi.ResetPasswordRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthServiceApi {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    AuthServiceApi authServiceApi = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/auth/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuthServiceApi.class);

    @POST("signin")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("signup")
    Call<User> register(@Body RegisterRequest registerRequest);
    @POST("forgot-password/send-otp")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest resetPassword);


}
