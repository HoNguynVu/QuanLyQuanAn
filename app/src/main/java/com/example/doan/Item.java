package com.example.doan;

public class Item {
    private final String itemName;
    private final String itemPrice;
    private final Integer itemImage;
    public Item(String itemName, String itemPrice, Integer itemImage) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
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
}
