package com.example.doan.DatabaseClass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cart_items")
public class FoodItem {
    @PrimaryKey(autoGenerate = true)
    private int cartId;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("category")
    private String category;
    @SerializedName("price")
    private double price;
    @SerializedName("image_url")
    private String image_url;
    @SerializedName("description")
    private String description;
    private String note;
    @SerializedName("available")
    private int available;
    @SerializedName("rating_avg")
    private float ratingAvg;
    private String itemQuantity;

    public FoodItem() {
    }

    public FoodItem(int id, String name, String category, double price, String image_url, int available, String description, float ratingAvg, String note, String itemQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.image_url = image_url;
        this.description = description;
        this.note = note;
        this.itemQuantity = itemQuantity;
        this.available = available;
        this.ratingAvg = ratingAvg;
    }

    public FoodItem(String name, String category, double price, String imageUrl, int available, String description, float ratingAvg) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
        this.image_url = imageUrl;
        this.description = description;
        this.ratingAvg = ratingAvg;
        note = "";
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
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


    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String newQuantity) {
        itemQuantity = newQuantity;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
