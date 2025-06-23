package com.example.doan.DatabaseClassResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartGetResponse {
    @SerializedName("items")
    public List<CartItem> items;

    public static class CartItem {
        @SerializedName("food_id")
        public int foodId;

        @SerializedName("quantity")
        public int quantity;

        @SerializedName("note")
        public String note;
    }
}

