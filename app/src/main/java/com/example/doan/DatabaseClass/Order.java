package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("order_id")
    private String orderId;

    @SerializedName("customer_name")
    private String customerName;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
}

