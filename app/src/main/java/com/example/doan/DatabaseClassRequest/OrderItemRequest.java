package com.example.doan.DatabaseClassRequest;

public class OrderItemRequest {
    public int food_id;
    public int quantity;

    public OrderItemRequest(int food_id, int quantity) {
        this.food_id = food_id;
        this.quantity = quantity;
    }
}