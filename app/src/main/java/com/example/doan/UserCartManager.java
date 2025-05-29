package com.example.doan;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.doan.DatabaseClass.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class UserCartManager {
    private static UserCartManager instance;
    private final List<FoodItem> cartItems;
    private double TotalOrder = 0;
    private OnTotalChangedListener listener;

    public interface OnTotalChangedListener {
        void onTotalChanged(double newTotal);
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
    private UserCartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized UserCartManager getInstance() {
        if(instance == null) {
            instance = new UserCartManager();
        }

        return instance;
    }

    public void addItem(FoodItem item) {
        cartItems.add(item);
        TotalOrder += item.getPrice() * Integer.parseInt(item.getItemQuantity());
        notifyTotalChanged();
    }

    public List<FoodItem> getCartItems() {
        return cartItems;
    }

    public double getTotalOrder() {
        return TotalOrder;
    }

    public void setTotalOrder(double newTotalOrder) {
        TotalOrder = newTotalOrder;
    }
}
