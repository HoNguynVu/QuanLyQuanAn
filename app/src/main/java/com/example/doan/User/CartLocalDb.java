package com.example.doan.User;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.doan.DatabaseClass.FoodItem;

@Database(entities = {FoodItem.class, CartMeta.class}, version = 5)
public abstract class CartLocalDb extends RoomDatabase {

    private static CartLocalDb instance;

    public abstract CartItemDao cartItemDao();
    public abstract CartMetaDao cartMetaDao();

    public static synchronized CartLocalDb getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CartLocalDb.class,
                    "cart_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

