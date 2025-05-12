package com.example.doan.Database;

public class OrderDetail {
    private String idOrder;
    private String idFood;
    private int quantity;

    public OrderDetail() {}

    public OrderDetail(String idOrder, String idFood, int quantity) {
        this.idOrder = idOrder;
        this.idFood = idFood;
        this.quantity = quantity;
    }

    public String getIdOrder() { return idOrder; }
    public void setIdOrder(String idOrder) { this.idOrder = idOrder; }

    public String getIdFood() { return idFood; }
    public void setIdFood(String idFood) { this.idFood = idFood; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

