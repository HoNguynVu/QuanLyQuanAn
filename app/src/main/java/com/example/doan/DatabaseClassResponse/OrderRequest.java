// OrderRequest.java
package com.example.doan.DatabaseClassResponse;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderRequest {
    @SerializedName("user_id")
    public int userId;

    @SerializedName("items")
    public List<Item> items;

    @SerializedName("discount_code")
    public String discountCode;

    @SerializedName("payment_method")
    public String paymentMethod;

    public static class Item {
        @SerializedName("food_id")
        public int foodId;

        @SerializedName("quantity")
        public int quantity;

        public Item(int foodId, int quantity) {
            this.foodId = foodId;
            this.quantity = quantity;
        }
    }
}
