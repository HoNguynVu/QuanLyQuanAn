package com.example.doan.User;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_meta")
public class CartMeta {

    @PrimaryKey
    public int id = 1; // luôn là 1, chỉ có 1 dòng duy nhất

    public double totalOrder;

    public CartMeta(double totalOrder) {
        this.totalOrder = totalOrder;
    }

    public double getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(double totalOrder) {
        this.totalOrder = totalOrder;
    }
}
