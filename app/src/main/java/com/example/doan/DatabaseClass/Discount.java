package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class Discount {

    @SerializedName("id")
    private int id;

    @SerializedName("code")
    private String code;

    @SerializedName("discount_percent")
    private int discountPercent;

    @SerializedName("max_discount_amount")
    private double maxDiscountAmount;

    @SerializedName("valid_from")
    private String validFrom; // có thể dùng Date nếu bạn convert định dạng ISO

    @SerializedName("valid_to")
    private String validTo;

    @SerializedName("is_active")
    private boolean isActive;

    // Getters & Setters

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public double getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters (nếu cần)

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setMaxDiscountAmount(double maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

}

