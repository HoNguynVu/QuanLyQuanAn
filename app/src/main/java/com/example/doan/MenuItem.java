package com.example.doan;

public class MenuItem {
    private String name;
    private int price;
    private String category;
    private String imageUrl;
    private boolean available;

    public MenuItem() {} // Constructor mặc định cho Firebase

    public MenuItem(String name, int price, String category, String imageUrl, boolean available) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    // Getters & Setters
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public boolean isAvailable() { return available; }
}
