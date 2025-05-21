package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class FoodItem {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("price")
    private double price;

    @SerializedName("available")
    private int available;

    @SerializedName("image_url")
    private String image_url;

    @SerializedName("description")
    private String description;

    @SerializedName("rating_avg")
    private float ratingAvg;

    public FoodItem() {}


    public FoodItem(String name, String category, double price, String imageUrl, int available, String description, float ratingAvg) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
        this.image_url = imageUrl;
        this.description = description;
        this.ratingAvg = ratingAvg;
    }

    // Chỉ có getter cho id vì id tự sinh từ MySQL
    public int getId() {
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

    public double getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public int getAvailable() {
        return available;
    }
    public void setAvailable(int available) {
        this.available = available;
    }

    public String getImageUrl() {
        return image_url;
    }
    public void setImageUrl(String imageUrl) {
        this.image_url = imageUrl;
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


