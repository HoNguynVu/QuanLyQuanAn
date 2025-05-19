package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("review_id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("food_id")
    private String foodId;

    @SerializedName("comment")
    private String comment;

    @SerializedName("rating")
    private float rating;

    @SerializedName("review_date")
    private String reviewDate;  // Có thể để String hoặc Date tùy xử lý

    public Review() {}

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }

    public String getReviewDate() {
        return reviewDate;
    }
}
