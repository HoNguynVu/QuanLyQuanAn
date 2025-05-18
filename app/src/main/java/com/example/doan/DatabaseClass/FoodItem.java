package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class FoodItem {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("price")
    private int price;

    @SerializedName("available")
    private boolean available;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("description")
    private String description;

    @SerializedName("rating_avg")
    private float ratingAvg;

    public FoodItem() {}

    public FoodItem(String name, String category, int price, boolean available,
                String imageUrl, String description, float ratingAvg) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
        this.imageUrl = imageUrl;
        this.description = description;
        this.ratingAvg = ratingAvg;
    }

    // Chỉ có getter cho id vì id tự sinh từ MySQL
    public String getId() {
        return id;
    }

    // Các getter và setter khác

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public float getRatingAvg() {
        return ratingAvg;
    }
    public void setRatingAvg(float ratingAvg) {
        this.ratingAvg = ratingAvg;
    }
}
