package com.example.doan.UserActivity;

import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;

import com.example.doan.Adapter.UserCartAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.User.UserCartManager;
import com.example.doan.User.UserSpaceItemDecoration;
import com.example.doan.databinding.UserActivityCartBinding;

import java.util.List;

public class UserCartActivity extends AppCompatActivity {
    private UserCartAdapter adapter;
    private UserActivityCartBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();
        setupCartListener();
        setBtnBack();
        setBtnCheckOut();

        // Gọi lại notify nếu dữ liệu đã có
        UserCartManager.getInstance().notifyTotalChanged();
    }

    private void setupRecyclerView() {
        adapter = new UserCartAdapter(UserCartManager.getInstance().getCartItems(), this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));
    }

    private void setupCartListener() {
        UserCartManager.getInstance().setOnTotalChangedListener(newTotal -> {
            Log.d(TAG, "onTotalChanged: " + newTotal);
            binding.totalOrder.setText(String.valueOf(newTotal));

            // Thông báo adapter cập nhật lại UI
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void setBtnBack() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    public void setBtnCheckOut() {
        binding.btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserCheckOutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CartDebug", "Cart size: " + UserCartManager.getInstance().getCartItems().size());

    }
}
