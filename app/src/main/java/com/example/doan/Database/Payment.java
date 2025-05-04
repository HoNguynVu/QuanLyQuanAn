package com.example.doan.Database;

public class Payment {
    private String paymentID;
    private String orderID;
    private double amount;
    private String method;
    private String time;
    private String status;

    public Payment() {}

    public Payment(String paymentID, String orderID, double amount, String method, String time, String status) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.amount = amount;
        this.method = method;
        this.time = time;
        this.status = status;
    }

    public String getPaymentID() { return paymentID; }
    public void setPaymentID(String paymentID) { this.paymentID = paymentID; }

    public String getOrderID() { return orderID; }
    public void setOrderID(String orderID) { this.orderID = orderID; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

