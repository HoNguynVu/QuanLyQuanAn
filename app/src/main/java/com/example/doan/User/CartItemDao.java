package com.example.doan.User;

import androidx.room.*;

import com.example.doan.DatabaseClass.FoodItem;

import java.util.List;

@Dao
public interface CartItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FoodItem item);

    @Query("SELECT * FROM cart_items")
    List<FoodItem> getAll();

    @Query("DELETE FROM cart_items")
    void clearCart();

    @Delete
    void delete(FoodItem item);

    @Update
    void update(FoodItem item);

    @Query("SELECT * FROM cart_items WHERE id = :cartId LIMIT 1")
    FoodItem findByCartId(int cartId);
}
