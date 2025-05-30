package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    private String orderId;
    @SerializedName("user_id")
    private int userId;

    @SerializedName("final_amount")
    private int finalAmount;

    @SerializedName("status")
    private String status;

    @SerializedName("created_at")
    private String createdAt;

    // Getter và Setter

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public int getUser_id() { return userId; }
    public void setUser_id(int user_id) { this.userId = user_id; }

}

