package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Discount implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("code")
    private String code;

    @SerializedName("discount_percent")
    private int discountPercent;

    @SerializedName("max_discount_amount")
    private double maxDiscountAmount;

    @SerializedName("valid_from")
    private Date validFrom; // có thể dùng Date nếu bạn convert định dạng ISO

    @SerializedName("valid_to")
    private Date validTo;

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

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
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

    public void setActive(boolean active) {
        isActive = active;
    }

}

