package com.example.doan.DatabaseClass;

public class Cart {
    private String userEmail;
    private String foodID;
    private int quantity;

    public Cart() {}

    public Cart(String userEmail, String foodID, int quantity) {
        this.userEmail = userEmail;
        this.foodID = foodID;
        this.quantity = quantity;
    }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getFoodID() { return foodID; }
    public void setFoodID(String foodID) { this.foodID = foodID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

