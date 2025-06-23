package com.example.doan.User;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClassRequest.CartSyncRequest;
import com.example.doan.DatabaseClassResponse.CartGetResponse;
import com.example.doan.DatabaseClassResponse.CartSyncResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

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


    public void syncCartToServer(Context context, int userId, Runnable onSynced) {
        List<CartSyncRequest.CartItem> syncItems = new ArrayList<>();
        for (FoodItem item : cartItems) {
            int foodId = item.getId();
            int quantity = Integer.parseInt(item.getItemQuantity());
            String note = item.getNote() != null ? item.getNote() : "";
            syncItems.add(new CartSyncRequest.CartItem(foodId, quantity, note));
        }

        CartSyncRequest request = new CartSyncRequest(userId, syncItems);

        APIService api = RetrofitClient.getRetrofitInstance().create(APIService.class);

        api.syncCart(request).enqueue(new Callback<CartSyncResponse>() {
            @Override
            public void onResponse(Call<CartSyncResponse> call, Response<CartSyncResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.d("SyncCart", "✅ Đồng bộ giỏ hàng thành công. Cart ID: " + response.body().getCartId());
                } else {
                    Log.e("SyncCart", "❌ Đồng bộ thất bại: " + response.code() + " - " + response.message());
                }

                // Gọi callback
                if (onSynced != null) onSynced.run();
            }

            @Override
            public void onFailure(Call<CartSyncResponse> call, Throwable t) {
                Log.e("SyncCart", "❌ Lỗi kết nối: " + t.getMessage());
                if (onSynced != null) onSynced.run();
            }
        });
    }

    public void loadCartFromServer(Context context, int userId, Runnable onLoaded) {
        APIService api = RetrofitClient.getRetrofitInstance().create(APIService.class);

        api.getCart(userId).enqueue(new Callback<CartGetResponse>() {
            @Override
            public void onResponse(Call<CartGetResponse> call, Response<CartGetResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    TotalOrder = 0;

                    List<CartGetResponse.CartItem> serverItems = response.body().items;
                    Log.d("CartLoad", "✅ Server trả về " + serverItems.size() + " món");
                    if (serverItems.isEmpty()) {
                        // Nếu không có món nào thì vẫn gọi callback
                        executor.execute(() -> {
                            CartLocalDb db = CartLocalDb.getInstance(context);
                            db.cartItemDao().clearCart();
                            db.cartMetaDao().insert(new CartMeta(0));
                            notifyTotalChanged();
                            if (onLoaded != null) onLoaded.run();
                        });
                        return;
                    }

                    final int totalItems = serverItems.size();
                    final int[] fetchedCount = {0};  // đếm số món đã lấy thành công

                    for (CartGetResponse.CartItem item : serverItems) {
                        Log.d("CartLoad", "🛒 Item: food_id=" + item.foodId +
                                ", quantity=" + item.quantity +
                                ", note=" + item.note);
                        api.getFoodByID(String.valueOf(item.foodId)).enqueue(new Callback<FoodItem>() {
                            @Override
                            public void onResponse(Call<FoodItem> call, Response<FoodItem> foodResponse) {
                                if (foodResponse.isSuccessful() && foodResponse.body() != null) {
                                    FoodItem food = foodResponse.body();
                                    food.setItemQuantity(String.valueOf(item.quantity));
                                    food.setNote(item.note);
                                    cartItems.add(food);
                                    TotalOrder += food.getPrice() * item.quantity;
                                } else {
                                    Log.e("GetFoodByID", "⚠️ Không lấy được thông tin món ăn ID=" + item.foodId);
                                }

                                fetchedCount[0]++;
                                if (fetchedCount[0] == totalItems) {
                                    saveCartToRoom(context, onLoaded);
                                }
                            }

                            @Override
                            public void onFailure(Call<FoodItem> call, Throwable t) {
                                Log.e("GetFoodByID", "❌ Lỗi kết nối món ID=" + item.foodId + ": " + t.getMessage());
                                fetchedCount[0]++;
                                if (fetchedCount[0] == totalItems) {
                                    saveCartToRoom(context, onLoaded);
                                }
                            }
                        });
                    }
                } else {
                    Log.e("GetCart", "❌ Server trả về lỗi: " + response.code());
                    if (onLoaded != null) onLoaded.run();
                }
            }

            @Override
            public void onFailure(Call<CartGetResponse> call, Throwable t) {
                Log.e("GetCart", "❌ Lỗi kết nối: " + t.getMessage());
                if (onLoaded != null) onLoaded.run();
            }
        });
    }

    private void saveCartToRoom(Context context, Runnable onLoaded) {
        executor.execute(() -> {
            CartLocalDb db = CartLocalDb.getInstance(context);
            db.cartItemDao().clearCart();
            for (FoodItem item : cartItems) {
                db.cartItemDao().insert(item);
            }
            db.cartMetaDao().insert(new CartMeta(TotalOrder));

            notifyTotalChanged();
            if (onLoaded != null) onLoaded.run();
        });
    }


}

