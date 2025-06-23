package com.example.doan.DatabaseClassRequest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartSyncRequest {
    @SerializedName("user_id")
    private int userId;

    @SerializedName("items")
    private List<CartItem> items;

    public CartSyncRequest(int userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items;
    }

    public static class CartItem {
        @SerializedName("food_id")
        private int foodId;

        @SerializedName("quantity")
        private int quantity;

        @SerializedName("note")
        private String note;

        public CartItem(int foodId, int quantity, String note) {
            this.foodId = foodId;
            this.quantity = quantity;
            this.note = note;
        }
    }
}
