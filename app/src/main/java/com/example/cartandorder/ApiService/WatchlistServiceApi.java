package com.example.cartandorder.ApiService;

import com.example.cartandorder.ReponseApi.CarResponse;
import com.example.cartandorder.ReponseApi.WatchlistResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface WatchlistServiceApi {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDate.parse(json.getAsString());
                }
            })
            .create();

    WatchlistServiceApi watchlistServiceApi = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WatchlistServiceApi.class);

    @PATCH("list/add/coin/{carId}")
    Call<WatchlistResponse> addCarToList(@Header("Authorization") String token, @Path("carId") Long carId);

    @GET("list")
    Call<WatchlistResponse> getList(@Header("Authorization") String token);

}
