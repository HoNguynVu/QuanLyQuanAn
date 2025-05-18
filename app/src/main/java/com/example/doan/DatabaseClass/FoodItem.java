package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class FoodItem {
    private int id;
    private String name;
    private int price;
    private String category;

    private String image_url;
    private boolean available;

    // ✅ Firebase key để xoá
    private String key;

    public FoodItem() {}

    public FoodItem(String name, int price, String category, String imageUrl, boolean available) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.image_url = imageUrl;
        this.available = available;
    }

    // ✅ Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return image_url; }
    public void setImageUrl(String imageUrl) { this.image_url = imageUrl; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}