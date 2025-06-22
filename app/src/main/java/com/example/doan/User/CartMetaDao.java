package com.example.doan.User; // hoặc package đúng của bạn

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.doan.User.CartMeta;

@Dao
public interface CartMetaDao {

    // Thêm hoặc cập nhật thông tin tổng hóa đơn
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartMeta meta);

    // Lấy thông tin tổng hóa đơn (chỉ có 1 dòng duy nhất với id = 1)
    @Query("SELECT * FROM cart_meta WHERE id = 1 LIMIT 1")
    CartMeta getMeta();
}
