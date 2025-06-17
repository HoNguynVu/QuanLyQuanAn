package com.example.doan.DatabaseClassResponse;

import com.example.doan.DatabaseClass.Discount;

public class DiscountResponse {
    private boolean success;
    private String message;
    private Discount discount;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Discount getDiscount() {
        return discount;
    }
}
