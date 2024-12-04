package com.example.cartandorder.comment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartandorder.ApiService.CommentServiceApi;
import com.example.cartandorder.R;
import com.example.cartandorder.ReponseApi.Comment;
import com.example.cartandorder.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ReviewViewHolder> {

    private List<com.example.cartandorder.ReponseApi.Comment> originalReviewList;
    private List<com.example.cartandorder.ReponseApi.Comment> filteredReviewList;
    private Context context;
    private Long userId;
    private Long carId;
    boolean isLikeToggled = false;
    boolean isDislikeToggled = false;
    public CommentAdapter(List<com.example.cartandorder.ReponseApi.Comment> reviewList, Context context,Long carId) {
        this.originalReviewList = reviewList;
        this.filteredReviewList = new ArrayList<>(originalReviewList);
        Log.e("DEBUG", "Original list size: " + originalReviewList.size());
        Log.e("DEBUG", "Filtered list initialized with size: " + filteredReviewList.size());
        this.context = context;
        this.carId=carId;
    }


    @NonNull
    @Override
    public CommentAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        com.example.cartandorder.ReponseApi.Comment review = filteredReviewList.get(position);

        Log.e("adapter",review.getContent());
        holder.tvName.setText(review.getUser().getFullName());
        holder.tvReview.setText(review.getContent());
        holder.tvLikeCount.setText(String.valueOf(review.getLikes()));
        holder.tvDislikeCount.setText(String.valueOf(review.getDislike()));
        Glide.with(holder.itemView.getContext())
                        .load(review.getUser().getAvatar())
                                .into(holder.image);
        float rating;
        if(review.getLikes()==0 &&review.getDislike()==0) {
            rating = 0;
        }else {
            rating = (float) review.getRating();
        }
        holder.ratingBar.setRating(rating);

        SharedPreferences sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);
        Log.e("userIdAdapter", userId.toString());
        Log.e("getLikeByUser", String.valueOf(review.getLikedByUsers().size()));
        if(review.getLikedByUsers().contains(userId)){
            holder.btnLike.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.blue));
        }
        if(review.getDislikedByUsers().contains(userId)){
            holder.btnDislike.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.blue));
        }

        String token = "Bearer " + getTokenFromPrefs();
        Log.e("carIdAdapterCOmment", carId.toString());
        holder.btnLike.setOnClickListener(v -> {
            CommentServiceApi.commentServiceApi.handleLike(review.getCommentId(),token).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
//                    Log.e("commentAdapter", response.body().getContent());
                    if (response.body() != null ) {
                        Long updatedLikeCount = response.body().getLikes();
                        Long updateDislike = response.body().getDislike();
                        holder.tvLikeCount.setText(String.valueOf(updatedLikeCount));
                        holder.tvDislikeCount.setText(String.valueOf(updateDislike));
                        isLikeToggled = !isLikeToggled;
                        holder.btnLike.setBackgroundTintList(ContextCompat.getColorStateList(context,
                                isLikeToggled ? R.color.blue : R.color.grey));
                        float star = (float)review.getRating();
                        holder.ratingBar.setRating(star);
                        if (isLikeToggled) {
                            isDislikeToggled = false;
                            holder.btnDislike.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                        }
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    Log.e("API_FAILURE", t.getMessage());
                    Toast.makeText(context, "Lỗi kết nối tới server", Toast.LENGTH_SHORT).show();
                }
            });
        });


        holder.btnDislike.setOnClickListener(v -> {
            CommentServiceApi.commentServiceApi.handleDisLike(review.getCommentId(),token).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if (response.body() != null) {
                        Long updatedDislikeCount = response.body().getDislike();
                        Long updatelike = response.body().getLikes();
                        holder.tvDislikeCount.setText(String.valueOf(updatedDislikeCount));
                        holder.tvLikeCount.setText(String.valueOf(updatelike));
                        isDislikeToggled = !isDislikeToggled;
                        holder.btnDislike.setBackgroundTintList(ContextCompat.getColorStateList(context,
                                isDislikeToggled ? R.color.blue : R.color.grey));
                        float star = (float)review.getRating();
                        holder.ratingBar.setRating(star);
                        if (isDislikeToggled) {
                            isLikeToggled = false;
                            holder.btnLike.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                        }
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    Log.e("API_FAILURE", t.getMessage());
                    Toast.makeText(context, "Lỗi kết nối tới server", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }



    @Override
    public int getItemCount() {
        Log.e("count", String.valueOf(filteredReviewList.size()));
        return filteredReviewList.size();
    }

    public void filterByStars(int stars) {
        filteredReviewList.clear();
        for (Comment review : originalReviewList) {
            if (Math.round(review.getRating()) == stars) {
                filteredReviewList.add(review);
            }
        }
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvReview, tvLikeCount, tvDislikeCount;
        RatingBar ratingBar;
        ImageView image;
        Button btnLike, btnDislike;



        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvReview = itemView.findViewById(R.id.tvReview);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvDislikeCount = itemView.findViewById(R.id.tvDislikeCount);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            image = itemView.findViewById(R.id.profile);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnDislike = itemView.findViewById(R.id.btnDislike);

        }
    }
    private String getTokenFromPrefs() {
        return context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                .getString("token", "");
    }
    public void updateData(List<Comment> newList) {
        this.originalReviewList = newList != null ? newList : new ArrayList<>();
        this.filteredReviewList = new ArrayList<>(originalReviewList);
        Log.e("DEBUG", "Data updated. Original size: " + originalReviewList.size() + ", Filtered size: " + filteredReviewList.size());
        notifyDataSetChanged();
    }

}
