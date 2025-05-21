package com.example.doan;

public class UserItem {
    private final String itemName;
    private final String itemPrice;
    private final Integer itemImage;
    private String itemQuantity;
    public UserItem(String itemName, String itemPrice, Integer itemImage, String itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public Integer getItemImage() {
        return itemImage;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String setItemQuantity(String newQuantity) {
        itemQuantity = newQuantity;
        return itemQuantity;
    }
}
