// OrderRequest.java
package com.example.doan.DatabaseClassRequest;

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

        @SerializedName("note")
        public String note;
        public Item(int foodId, int quantity, String note) {
            this.foodId = foodId;
            this.quantity = quantity;
            this.note = note;
        }
    }
}
