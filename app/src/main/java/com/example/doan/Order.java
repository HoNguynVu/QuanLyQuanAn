package com.example.doan;

public class Order {
    public String id, customerName, orderTime, status;
    public double totalAmount;

    public Order(String id, String customerName, String orderTime, double totalAmount, String status) {
        this.id = id;
        this.customerName = customerName;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.status = status;
    }
}