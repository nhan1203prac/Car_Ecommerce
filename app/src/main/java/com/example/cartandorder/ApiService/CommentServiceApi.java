package com.example.cartandorder.ApiService;

import com.example.cartandorder.ReponseApi.CarCreateResponse;
import com.example.cartandorder.ReponseApi.Comment;
import com.example.cartandorder.RequestApi.CarCreateRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentServiceApi {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDate.parse(json.getAsString());
                }
            })
            .create();

    CommentServiceApi commentServiceApi = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CommentServiceApi.class);

    @POST("{carId}/like")
    Call<Comment> handleLike(@Path("carId") Long carId, @Header("Authorization") String token);


    @POST("{carId}/dislike")
    Call<Comment> handleDisLike(@Path("carId") Long carId, @Header("Authorization") String token);

    @GET("comment/car/{carId}")
    Call<List<Comment>> getComment(@Path("carId") Long carId, @Header("Authorization") String token);
    @GET("comment/car/{carId}/rating/{rating}")
    Call<List<Comment>> getCommentByRating(@Path("carId") Long carId,@Path("rating") int rating);
}
