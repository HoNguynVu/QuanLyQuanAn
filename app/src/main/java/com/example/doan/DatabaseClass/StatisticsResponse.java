package com.example.doan.DatabaseClass;

import com.google.gson.annotations.SerializedName;

public class StatisticsResponse {
    @SerializedName("total_orders")
    private int totalOrders;

    @SerializedName("today_revenue")
    private double todayRevenue;

    @SerializedName("monthly_revenue")
    private double monthlyRevenue;

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTodayRevenue() {
        return todayRevenue;
    }

    public double getMonthlyRevenue() {
        return monthlyRevenue;
    }
}
