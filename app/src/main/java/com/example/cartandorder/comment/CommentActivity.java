package com.example.cartandorder.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartandorder.ApiService.CommentServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.Comment;
import com.example.cartandorder.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter reviewAdapter;
    private List<Comment> reviewList;
    private Button btnBack;
    ImageView profileImageView;
    private Long carId;
    Spinner starFilterSpinner;
    Button filterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        String token = "Bearer " + getTokenFromPrefs();
        starFilterSpinner = findViewById(R.id.starFilterSpinner);
        filterButton = findViewById(R.id.filterButton);
        Log.e("token", token);
        Intent in = getIntent();
        carId = in.getLongExtra("carId",-1);
        Log.e("carid comment", carId.toString());
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        reviewList = new ArrayList<>();

//        Log.e("outof func ",reviewList.get(0).getContent());
        reviewAdapter = new CommentAdapter(reviewList,CommentActivity.this,carId);
        recyclerView.setAdapter(reviewAdapter);
        getComment(token,carId);



//        filterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int selectedStars = Integer.parseInt(starFilterSpinner.getSelectedItem().toString());
//                reviewAdapter.filterByStars(selectedStars); // Lọc theo số sao
//            }
//        });
        filterButton.setOnClickListener(v->{
            int selectedStars = Integer.parseInt(starFilterSpinner.getSelectedItem().toString());
            reviewAdapter.filterByStars(selectedStars);
        });
        btnBack.setOnClickListener(v->{
            Intent intent = new Intent(CommentActivity.this, DetailActivity.class);
            intent.putExtra("carId", carId);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        });
    }

    private void getComment(String token,Long carId){
        CommentServiceApi.commentServiceApi.getComment(carId,token).enqueue(new Callback<List<com.example.cartandorder.ReponseApi.Comment>>() {
            @Override
            public void onResponse(Call<List<com.example.cartandorder.ReponseApi.Comment>> call, Response<List<com.example.cartandorder.ReponseApi.Comment>> response) {
                Log.e("---comments carId ", String.valueOf(response.body().size()));
                if(response.isSuccessful()&&response.body()!=null){
                    for(com.example.cartandorder.ReponseApi.Comment comment:response.body()){
                        Comment newComment= new Comment(comment.getCommentId(),comment.getUser(),comment.getCar(),comment.getContent(),comment.getRating(),comment.getLikes(),comment.getDislike(),comment.getLikedByUsers(),comment.getDislikedByUsers(),comment.getCreatedAt());

                        reviewList.add(newComment);

                    }
                    reviewAdapter.updateData(reviewList);

                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<com.example.cartandorder.ReponseApi.Comment>> call, Throwable t) {
                Log.e("API_FAILURE", t.getMessage());
                Toast.makeText(CommentActivity.this,"call Api failure",Toast.LENGTH_SHORT);
            }
        });
    }
    private String getTokenFromPrefs() {
        return getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }


}