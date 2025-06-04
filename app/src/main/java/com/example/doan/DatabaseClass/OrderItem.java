package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("id")
    private String id;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("food_id")
    private String foodId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price")
    private double price;

    public OrderItem() {}

    public OrderItem(String id, String orderId, String foodId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public OrderItem(String id, String orderId, String foodId, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.price = price;
    }

    // getter/setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFoodId() {
        return foodId;
    }
    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
