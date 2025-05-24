package com.example.doan;

public class UserItem {
    private int id;
    private final String name;
    private final String category;
    private final double price;
    private final String image_url;
    private final String description;
    private String itemQuantity;
    public UserItem(int id, String name, String category, double price, String image_url, String description, String itemQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.image_url = image_url;
        this.description = description;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return name;
    }

    public double getItemPrice() {
        return price;
    }

    public String getImageUrl() {
        return image_url;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String newQuantity) {
        itemQuantity = newQuantity;
    }
}
