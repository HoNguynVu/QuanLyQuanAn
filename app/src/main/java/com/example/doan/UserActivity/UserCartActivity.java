package com.example.doan.UserActivity;

import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
            if(UserCartManager.getInstance().getTotalOrder() > 0) {
                binding.btnCheckout.setEnabled(false);
                Intent intent = new Intent(this, UserCheckOutActivity.class);
                startActivity(intent);
                // Bật lại sau 500ms
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    binding.btnCheckout.setEnabled(true);
                }, 500);
            }
            else {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CartDebug", "Cart size: " + UserCartManager.getInstance().getCartItems().size());

    }
}
