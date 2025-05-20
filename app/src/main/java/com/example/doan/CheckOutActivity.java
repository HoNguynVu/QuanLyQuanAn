package com.example.doan;

import static android.content.ContentValues.TAG;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;

import com.example.doan.Adapter.CartAdapter;
import com.example.doan.Adapter.CheckOutAdapter;
import com.example.doan.databinding.ActivityCheckOutBinding;

import java.util.List;

public class CheckOutActivity extends AppCompatActivity {
    List<Item> cartList = CartManager.getInstance().getCartItems();
    CheckOutAdapter adapter;
    private ActivityCheckOutBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new CheckOutAdapter(cartList, this);
        binding.recyclerviewList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
        );

        binding.recyclerviewList.addItemDecoration(dividerItemDecoration);

        binding.btnBack.setOnClickListener(v -> finish());

        String price = CartManager.getInstance().getTotalOrder() + "$";
        binding.price.setText(price);
        binding.fee.setText("0$");
        binding.totalOrder.setText(price);
    }
}