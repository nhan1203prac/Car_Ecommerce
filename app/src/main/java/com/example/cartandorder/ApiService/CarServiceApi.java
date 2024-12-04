package com.example.cartandorder.ApiService;

import com.example.cartandorder.ReponseApi.CarCreateResponse;
import com.example.cartandorder.ReponseApi.CarResponse;
import com.example.cartandorder.ReponseApi.CarWatchlistResponse;
import com.example.cartandorder.ReponseApi.LoginResponse;
import com.example.cartandorder.RequestApi.CarCreateRequest;
import com.example.cartandorder.RequestApi.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CarServiceApi {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDate.parse(json.getAsString()); // Chuyển chuỗi thành LocalDate
                }
            })
            .create();

    CarServiceApi carServiceApi = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CarServiceApi.class);

    @POST("car")
    Call<CarCreateResponse> create( @Header("Authorization") String token,
                                   @Body CarCreateRequest carCreateRequest);

    @GET("car")
    Call<List<CarWatchlistResponse>> getListCarBrand(@Header("Authorization") String token, @Query("brand") String brand);

    @GET("car/{carId}")
    Call<CarCreateResponse> getCarById(@Header("Authorization") String token, @Path("carId") Long carId);

    @GET("car/search")
    Call<List<CarWatchlistResponse>> searchCar(@Header("Authorization") String token,@Query("name") String name);
}
