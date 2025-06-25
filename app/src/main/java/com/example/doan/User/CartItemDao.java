package com.example.doan.User;

import androidx.room.*;

import com.example.doan.DatabaseClass.FoodItem;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert
    long insert(FoodItem item);

    @Query("SELECT * FROM cart_items")
    List<FoodItem> getAll();

    @Query("DELETE FROM cart_items")
    void clearCart();

    @Delete
    void delete(FoodItem item);
    @Update
    void update(FoodItem item);

    @Query("SELECT * FROM cart_items WHERE localId = :Id LIMIT 1")
    FoodItem findByLocalId(int Id);

    @Query("DELETE FROM cart_items WHERE localId = :Id")
    void deleteByLocalId(int Id);

    @Query("SELECT COUNT(*) FROM cart_items")
    int getCartItemCount();
}

