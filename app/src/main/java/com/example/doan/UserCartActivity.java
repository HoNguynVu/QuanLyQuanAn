package com.example.doan;

import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;

import com.example.doan.Adapter.UserCartAdapter;
import com.example.doan.databinding.UserActivityCartBinding;

import java.util.List;

public class UserCartActivity extends AppCompatActivity {
    List<UserItem> cartList = UserCartManager.getInstance().getCartItems();
    UserCartAdapter adapter;
    private UserActivityCartBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserCartManager userCartManager = UserCartManager.getInstance();
        userCartManager.setOnTotalChangedListener(new UserCartManager.OnTotalChangedListener() {
            @Override
            public void onTotalChanged(double newTotal) {
                // Cập nhật TextView mỗi khi total thay đổi
                String total = newTotal + "$";
                Log.d(TAG, "onTotalChanged: " + newTotal);
                binding.totalOrder.setText(total);
            }
        });

        userCartManager.notifyTotalChanged();

        adapter = new UserCartAdapter(cartList, this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(dpToPx(16)));

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnCheckout.setOnClickListener(v -> {
                Intent intent = new Intent(this, UserCheckOutActivity.class);
                startActivity(intent);
        });
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}