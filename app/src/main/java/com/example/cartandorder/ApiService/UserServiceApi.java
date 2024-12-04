package com.example.cartandorder.ApiService;

import com.example.cartandorder.ReponseApi.LoginResponse;
import com.example.cartandorder.ReponseApi.User;
import com.example.cartandorder.RequestApi.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServiceApi {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    UserServiceApi userServiceApi = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserServiceApi.class);

    @GET("user/{userId}")
    Call<User> getUserId(@Path("userId") Long userId, @Header("Authorization") String token);
    @PATCH("user/profile")
    Call<User> updateUser(@Header("Authorization") String token,@Body User user);
}
