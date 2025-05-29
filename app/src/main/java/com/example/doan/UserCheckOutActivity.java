package com.example.doan;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan.Adapter.UserCheckOutAdapter;
import com.example.doan.databinding.UserActivityCheckOutBinding;

import java.util.List;

public class UserCheckOutActivity extends AppCompatActivity {
    List<UserItem> cartList = UserCartManager.getInstance().getCartItems();
    UserCheckOutAdapter adapter;
    private UserActivityCheckOutBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new UserCheckOutAdapter(cartList, this);
        binding.recyclerviewList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
        );

        binding.recyclerviewList.addItemDecoration(dividerItemDecoration);

        binding.btnBack.setOnClickListener(v -> finish());

        String price = UserCartManager.getInstance().getTotalOrder() + "$";
        binding.price.setText(price);
        binding.fee.setText("0$");
        binding.totalOrder.setText(price);

        binding.btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(UserCheckOutActivity.this, UserOrderSuccessActivity.class);
            startActivity(intent);
        });
    }
}