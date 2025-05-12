package com.example.doan.Database;

public class Order {
    private String idOrder;
    private String userEmail;
    private String time;
    private double totalPrice;
    private String status;

    public Order() {}

    public Order(String idOrder, String userEmail, String time, double totalPrice, String status) {
        this.idOrder = idOrder;
        this.userEmail = userEmail;
        this.time = time;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getIdOrder() { return idOrder; }
    public void setIdOrder(String idOrder) { this.idOrder = idOrder; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}

