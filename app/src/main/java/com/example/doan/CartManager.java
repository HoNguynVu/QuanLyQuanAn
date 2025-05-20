package com.example.doan;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.doan.Adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Item> cartItems;
    private int TotalOrder = 0;
    private OnTotalChangedListener listener;

    public interface OnTotalChangedListener {
        void onTotalChanged(int newTotal);
    }

    public void setOnTotalChangedListener(OnTotalChangedListener listener) {
        this.listener = listener;
    }
    public void notifyTotalChanged() {
        if (listener != null) {
            listener.onTotalChanged(TotalOrder);
        }
        else {
            Log.d(TAG, "listener null");
        }
    }
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
        cartItems.add(item);
        TotalOrder += Integer.parseInt(item.getItemPrice().substring(0, item.getItemPrice().length() - 1)) * Integer.parseInt(item.getItemQuantity());
        notifyTotalChanged();
    }

    public List<Item> getCartItems() {
        return cartItems;
    }

    public int getTotalOrder() {
        return TotalOrder;
    }

    public void setTotalOrder(int newTotalOrder) {
        TotalOrder = newTotalOrder;
    }
}
