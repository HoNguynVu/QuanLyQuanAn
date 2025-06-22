package com.example.doan.User;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.example.doan.DatabaseClass.FoodItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserCartManager {
    private static UserCartManager instance;
    private final List<FoodItem> cartItems;
    private double TotalOrder = 0;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private OnTotalChangedListener listener;

    public interface OnTotalChangedListener {
        void onTotalChanged(double newTotal);
    }

    public interface OnItemAddedListener {
        void onItemAdded(FoodItem itemWithId);
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

    public void initialize(Context context, Runnable onCartLoaded) {
        executor.execute(() -> {
            // Load danh sách item từ Room
            List<FoodItem> itemList = CartLocalDb.getInstance(context)
                    .cartItemDao()
                    .getAll();

            this.cartItems.clear();
            this.cartItems.addAll(itemList);

            // Load totalOrder từ CartMeta
            CartMeta meta = CartLocalDb.getInstance(context)
                    .cartMetaDao()
                    .getMeta();

            if (meta != null) {
                this.TotalOrder = meta.getTotalOrder();
                Log.d("CartMeta", "Loaded meta: " + meta.getTotalOrder());
            } else {
                this.TotalOrder = 0;
            }


            // Gọi callback để cập nhật UI
            if (onCartLoaded != null) {
                onCartLoaded.run();
            }
        });
    }

    public void addItem(Context context, FoodItem item, OnItemAddedListener callback) {
        cartItems.add(item);
        TotalOrder += item.getPrice() * Integer.parseInt(item.getItemQuantity());
        TotalOrder = Math.max(0, TotalOrder);
        notifyTotalChanged();
        Log.d("Total Order", String.valueOf(TotalOrder));

        executor.execute(() -> {
            // Lấy cartId mới
            long generatedId = CartLocalDb.getInstance(context).cartItemDao().insert(item);
            item.setCartId((int) generatedId); // Gán lại cartId vào item

            Log.d("CartID", "Generated CartID: " + generatedId);

            // Lưu meta
            CartLocalDb.getInstance(context).cartMetaDao()
                    .insert(new CartMeta(TotalOrder));

            if (callback != null) {
                callback.onItemAdded(item);  // Trả kết quả về cho UI
            }
        });
    }

    public void deleteItem(Context context, FoodItem item) {
        cartItems.remove(item);
        TotalOrder -= item.getPrice() * Integer.parseInt(item.getItemQuantity());
        notifyTotalChanged();
        executor.execute(() ->
            CartLocalDb.getInstance(context).cartItemDao().delete(item)
        );
        executor.execute(() -> {
            CartLocalDb.getInstance(context).cartMetaDao()
                    .insert(new CartMeta(TotalOrder));
        });
    }

    public void cleanCart(Context context) {
        cartItems.clear();
        TotalOrder = 0;
        notifyTotalChanged();
        executor.execute(() ->
                CartLocalDb.getInstance(context).cartItemDao().clearCart()
        );

        executor.execute(() -> {
            CartLocalDb.getInstance(context).cartMetaDao()
                    .insert(new CartMeta(TotalOrder));
        });
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

    public void updateItemQuantity(Context context, FoodItem newItem, double newTotal) {
        boolean found = false;

        for (int i = 0; i < cartItems.size(); i++) {
            FoodItem item = cartItems.get(i);
            if (item.getId() == newItem.getId()) {
                item.setItemQuantity(newItem.getItemQuantity());
                cartItems.set(i, item); // cập nhật trong danh sách
                found = true;
                break;
            }
        }

        if (!found) {
            cartItems.add(newItem);
        }

        TotalOrder = newTotal;

        // Lưu lại vào Room DB
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                CartLocalDb db = CartLocalDb.getInstance(context);
                db.cartItemDao().insert(newItem); // chỉ lưu 1 món, Room sẽ thay thế nếu trùng id
                db.cartMetaDao().insert(new CartMeta(TotalOrder));
            } catch (Exception e) {
                Log.e("CartUpdate", "Error inserting item: " + e.getMessage());
            }
        });

        notifyTotalChanged();
    }


}
