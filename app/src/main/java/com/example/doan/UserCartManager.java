package com.example.doan;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserCartManager {
    private static UserCartManager instance;
    private final List<UserItem> cartItems;
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
    private UserCartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized UserCartManager getInstance() {
        if(instance == null) {
            instance = new UserCartManager();
        }

        return instance;
    }

    public void addItem(UserItem item) {
        cartItems.add(item);
        TotalOrder += Integer.parseInt(item.getItemPrice().substring(0, item.getItemPrice().length() - 1)) * Integer.parseInt(item.getItemQuantity());
        notifyTotalChanged();
    }

    public List<UserItem> getCartItems() {
        return cartItems;
    }

    public int getTotalOrder() {
        return TotalOrder;
    }

    public void setTotalOrder(int newTotalOrder) {
        TotalOrder = newTotalOrder;
    }
}
