package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doan.databinding.UserActivityDetailsBinding;

public class UserDetailsActivity extends AppCompatActivity {
    UserActivityDetailsBinding binding;
    UserCartManager userCartManager = UserCartManager.getInstance();
    int quantity;
    public UserDetailsActivity() {
        // Constructor mặc định
    }

    public UserDetailsActivity(UserActivityDetailsBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = UserActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String foodName = intent.getStringExtra("MenuItemName");
        double foodPrice = intent.getDoubleExtra("MenuItemPrice", 0);
        String foodQuantity = intent.getStringExtra("MenuItemQuantity");
        String foodImageUrl = intent.getStringExtra("MenuImageUrl");

        assert foodQuantity != null;
        quantity = Integer.parseInt(foodQuantity);
        String Total = (foodPrice * quantity) + "$";

        binding.detailsFoodName.setText(foodName);
        binding.detailsFoodPrice.setText(String.valueOf(foodPrice));
        Glide.with(this).load(foodImageUrl).into(binding.detailsFoodImage);
        binding.quantity.setText(foodQuantity);
        binding.btnBack.setOnClickListener(v -> finish());
        binding.total.setText(Total);


        binding.btnMinus.setOnClickListener(v -> {
            if(quantity > 1) {
                quantity--;

                binding.quantity.setText(String.valueOf(quantity));
                String total = quantity * foodPrice + "$";
                binding.total.setText(total);

            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            binding.quantity.setText(String.valueOf(quantity));
            String total = quantity * foodPrice + "$";
            binding.total.setText(total);
        });

        binding.btnOrder.setOnClickListener(v -> {
            UserCartManager.getInstance().addItem(new UserItem(1, foodName, "", foodPrice, foodImageUrl, "", String.valueOf(quantity)));
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }
}