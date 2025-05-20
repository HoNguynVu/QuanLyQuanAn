package com.example.doan;

import static android.content.ContentValues.TAG;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;

import com.example.doan.Adapter.CartAdapter;
import com.example.doan.databinding.ActivityCartBinding;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    List<Item> cartList = CartManager.getInstance().getCartItems();
    CartAdapter adapter;
    private ActivityCartBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CartManager cartManager = CartManager.getInstance();
        cartManager.setOnTotalChangedListener(new CartManager.OnTotalChangedListener() {
            @Override
            public void onTotalChanged(int newTotal) {
                // Cập nhật TextView mỗi khi total thay đổi
                String total = newTotal + "$";
                Log.d(TAG, "onTotalChanged: " + newTotal);
                binding.totalOrder.setText(total);
            }
        });

        cartManager.notifyTotalChanged();

        adapter = new CartAdapter(cartList, this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(dpToPx(16)));

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnCheckout.setOnClickListener(v -> {
                Intent intent = new Intent(this, CheckOutActivity.class);
                startActivity(intent);
        });
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}