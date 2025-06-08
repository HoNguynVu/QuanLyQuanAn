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

    @SerializedName("note")
    private String note;

    public OrderItem() {}

    public OrderItem(String id, String orderId, String foodId, int quantity, String note) {
        this.id = id;
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.note = note;
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

    public String getNote() {
        return note;
    }
    public void setNote(String note) { this.note = note; }
}
