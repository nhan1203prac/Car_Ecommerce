package com.example.cartandorder.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartandorder.ApiService.CallServiceApi;
import com.example.cartandorder.ApiService.CarServiceApi;
import com.example.cartandorder.ApiService.CommentServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.CarCreateResponse;
import com.example.cartandorder.ReponseApi.CarResponse;
import com.example.cartandorder.ReponseApi.Comment;
import com.example.cartandorder.RequestApi.CallRequest;
import com.example.cartandorder.comment.CommentActivity;
import com.example.cartandorder.contact.ChatActivity;
import com.example.cartandorder.contact.chatAndCall.CallItem;
import com.example.cartandorder.fragments.HomeFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SlideImageAdapter imageSliderAdapter;
    private List<String> images;
    private Long carId;
    private Comment comment;
    Button btnNext,btnPrev;
    Button btnCall, btnSend ;
    Button btnBack, btnColor,btnLike,btnDislike;
    ImageView image;
    TextView seeMore,name,countReview,desc,commentName,content,likeCount,dislikeCount,price;
    RatingBar countStar;
    String numberPhone;
    final int[] currentPosition = {0};
    int totalItems;
    String color;
    boolean isLikeToggled = false;
    boolean isDislikeToggled = false;
    private Long userId;
    private Long userId2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);

        String token = "Bearer " + getTokenFromPrefs();
        Log.e("userId",userId.toString());
        Intent intent = getIntent();
        carId = intent.getLongExtra("carId",-1);
        color = intent.getStringExtra("color");



        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnCall = findViewById(R.id.btnCall);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        btnColor = findViewById(R.id.color);
        seeMore = findViewById(R.id.seeMore);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        countReview = findViewById(R.id.countReview);
        price = findViewById(R.id.priceCar);
        images = new ArrayList<>();
        commentName = findViewById(R.id.commentName);
        content = findViewById(R.id.content);
        likeCount = findViewById(R.id.likeCount);
        dislikeCount = findViewById(R.id.dislikeCount);
        countStar = findViewById(R.id.countStar);
        btnLike = findViewById(R.id.btnLike);
        btnDislike = findViewById(R.id.btnDislike);

        recyclerView = findViewById(R.id.recyclerView);
        imageSliderAdapter = new SlideImageAdapter(images);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(imageSliderAdapter);
        callApi(token);

        btnNext.setOnClickListener(v -> {
            if (currentPosition[0] < totalItems - 1) {
                currentPosition[0]++;
                recyclerView.smoothScrollToPosition(currentPosition[0]);
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPosition[0] > 0) {
                currentPosition[0]--;
                recyclerView.smoothScrollToPosition(currentPosition[0]);
            }
        });

        btnCall.setOnClickListener(v->{
            Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+numberPhone));
            if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(DetailActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                return;
            }
            fetchApiCall(userId,userId2);
            startActivity(intentCall);
        });

        btnSend.setOnClickListener(v->{
            Intent navigateToChatPage = new Intent(DetailActivity.this, ChatActivity.class);
            navigateToChatPage.putExtra("userId2",userId2);
            startActivity(navigateToChatPage);
        });
        seeMore.setOnClickListener(v->{
            Intent navigateToReviewPage = new Intent(DetailActivity.this, CommentActivity.class);
            Log.e("carId detail", carId.toString());
            navigateToReviewPage.putExtra("carId",carId);
            startActivity(navigateToReviewPage);
        });
        btnBack.setOnClickListener(v->{

            finish();
        });
        btnLike.setOnClickListener(v -> {
            Log.e("comment",String.valueOf(comment.getLikes()));
            if (comment != null) {
                CommentServiceApi.commentServiceApi.handleLike(comment.getCommentId(), token).enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.body() != null ) {
                            Long updatedLikeCount = response.body().getLikes();
                            Long updateDislike = response.body().getDislike();
                            likeCount.setText(String.valueOf(updatedLikeCount));
                            dislikeCount.setText(String.valueOf(updateDislike));
                            isLikeToggled = !isLikeToggled;
                            btnLike.setBackgroundTintList(ContextCompat.getColorStateList(DetailActivity.this,
                                    isLikeToggled ? R.color.blue : R.color.grey));
                            float star = (float)comment.getRating();
                            countStar.setRating(star);
                            if (isLikeToggled) {
                                isDislikeToggled = false;
                                btnDislike.setBackgroundTintList(ContextCompat.getColorStateList(DetailActivity.this, R.color.grey));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Log.e("API_FAILURE", t.getMessage());
                        Toast.makeText(DetailActivity.this, "Lỗi kết nối tới server", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(DetailActivity.this, "Không có bình luận nào để thích", Toast.LENGTH_SHORT).show();
            }
        });

        btnDislike.setOnClickListener(v -> {
            Log.e("comment",String.valueOf(comment.getLikes()));
            if (comment != null ) {
                CommentServiceApi.commentServiceApi.handleDisLike(comment.getCommentId(), token).enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.body() != null) {
                            Long updatedDislikeCount = response.body().getDislike();
                            Long updatelike = response.body().getLikes();
                            dislikeCount.setText(String.valueOf(updatedDislikeCount));
                            likeCount.setText(String.valueOf(updatelike));
                            isDislikeToggled = !isDislikeToggled;
                            btnDislike.setBackgroundTintList(ContextCompat.getColorStateList(DetailActivity.this,
                                    isDislikeToggled ? R.color.blue : R.color.grey));
                            float star = (float)comment.getRating();
                            countStar.setRating(star);
                            if (isDislikeToggled) {
                                isLikeToggled = false;
                                btnLike.setBackgroundTintList(ContextCompat.getColorStateList(DetailActivity.this, R.color.grey));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Log.e("API_FAILURE", t.getMessage());
                        Toast.makeText(DetailActivity.this, "Lỗi kết nối tới server", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {

                Toast.makeText(DetailActivity.this, "Không có bình luận nào để không thích", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void callApi(String token){
            Log.e("---------carId","carId" + carId);
            Log.e("---------API", "token: " + token);
            CarServiceApi.carServiceApi.getCarById(token,carId).enqueue(new Callback<CarCreateResponse>() {
                @Override
                public void onResponse(Call<CarCreateResponse> call, Response<CarCreateResponse> response) {

                        Log.e("---------API", "call success " );
                        Glide.with(DetailActivity.this)
                                .load(response.body().getImages().get(0))
                                .into(image);
                        int colorResId = getResources().getIdentifier(color, "color", getPackageName());

                        if (colorResId != 0) {

                            btnColor.setBackgroundTintList(ContextCompat.getColorStateList(DetailActivity.this, colorResId));
                        } else {
                            Log.e("DetailActivity", "Color not found in resources");
                        }
                        name.setText(response.body().getName());
                        long count = response.body().getComments().size();
                        countReview.setText("(" + count + " Reviews)");

                        userId2 = response.body().getUser().getUserId();

                        desc.setText(response.body().getDescription());
                        for(String url:response.body().getImages()){
                            images.add(url);
                        }
                        totalItems = images.size();
                        imageSliderAdapter.notifyDataSetChanged();



                        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Ví dụ: 1,234.56
                        String formattedPrice = decimalFormat.format(response.body().getPrice());
                        price.setText(formattedPrice +" VND");

                        numberPhone = response.body().getUser().getPhoneNumber();
                        List<Comment> comments = response.body().getComments();
                        if (comments != null && !comments.isEmpty()) {

                            comment = comments.get(0);
                            commentName.setText(comment.getUser().getFullName());
                            content.setText(comment.getContent());
                            likeCount.setText(String.valueOf(comment.getLikes()));
                            dislikeCount.setText(String.valueOf(comment.getDislike()));
                            if(comment.getDislikedByUsers().contains(userId)){
                                btnDislike.setBackgroundTintList(ContextCompat.getColorStateList(DetailActivity.this, R.color.blue));
                            }
                            if(comment.getLikedByUsers().contains(userId)){
                                btnLike.setBackgroundTintList(ContextCompat.getColorStateList(DetailActivity.this, R.color.blue));
                            }
                            float rating;
                            if(comment.getLikes()==0 &&comment.getDislike()==0) {
                                rating = 0;
                            }else {
                                rating = (float) comment.getRating();
                            }
                            countStar.setRating(rating);
                        } else {

                            Log.e("DetailActivity", "Danh sách comment trống");
                            commentName.setText("Không có bình luận");
                            content.setText("");
                            likeCount.setText("0");
                            dislikeCount.setText("0");
                            countStar.setRating(0);
                        }


                    Toast.makeText(DetailActivity.this,"call Api success",Toast.LENGTH_SHORT);

                }

                @Override
                public void onFailure(Call<CarCreateResponse> call, Throwable t) {
                    Log.e("API_FAILURE", t.getMessage());
                    Toast.makeText(DetailActivity.this,"call Api failure",Toast.LENGTH_SHORT);
                }
            });

    }
    private String getTokenFromPrefs() {
        return getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }
    private void fetchApiCall(Long senderId, Long receiverId){
        CallRequest callRequest = new CallRequest(senderId,receiverId);
        CallServiceApi.CallServiceApi.createCall(callRequest).enqueue(new Callback<CallItem>() {
            @Override
            public void onResponse(Call<CallItem> call, Response<CallItem> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Log.e("Call success", "Perform call");
                }
                else{
                    Log.e("API Error", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CallItem> call, Throwable t) {
                Log.e("API Error", "Request failed: " + t.getMessage());
            }
        });
    }
}
