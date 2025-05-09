package com.example.doan.DatabaseClass;

public class FoodItem {
    private String name;
    private int price;
    private String category;
    private String imageUrl;
    private boolean available;

    // ✅ Firebase key để xoá
    private String key;

    public FoodItem() {}

    public FoodItem(String name, int price, String category, String imageUrl, boolean available) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    // ✅ Getter và Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}