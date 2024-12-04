package com.example.cartandorder.comment;

public class Comment {
    private String name;
    private String reviewText;
    private int rating;
    private int likeCount;
    private int dislikeCount;
    private String imageProfile;

    public Comment(String name, String reviewText, int rating, int likeCount, int dislikeCount,String imageProfile) {
        this.name = name;
        this.reviewText = reviewText;
        this.rating = rating;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.imageProfile = imageProfile;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getRating() {
        return rating;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }
    public String getImageProfile(){
        return imageProfile;
    }
}

