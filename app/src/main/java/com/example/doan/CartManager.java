package com.example.doan;

import com.example.doan.Adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Item> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if(instance == null) {
            instance = new CartManager();
        }

        return instance;
    }

    public void addItem(Item item) {
        for(Item i : cartItems) {
            if(i.getItemName().equals(item.getItemName())) {
                return;
            }
        }

        cartItems.add(item);
    }

    public List<Item> getCartItems() {
        return cartItems;
    }
}
