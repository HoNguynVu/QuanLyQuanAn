package com.example.doan;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.databinding.ActivityDetailsBinding;

public class detailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;
    CartManager cartManager = CartManager.getInstance();
    int quantity;
    public detailsActivity() {
        // Constructor mặc định
    }

    public detailsActivity(ActivityDetailsBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String foodName = intent.getStringExtra("MenuItemName");
        String foodPrice = intent.getStringExtra("MenuItemPrice");
        String foodQuantity = intent.getStringExtra("MenuItemQuantity");
        int foodImage = intent.getIntExtra("MenuItemImage", 0);

        assert foodPrice != null;
        int price = Integer.parseInt(foodPrice.substring(0, foodPrice.length() - 1));
        assert foodQuantity != null;
        quantity = Integer.parseInt(foodQuantity);
        String Total = (price * quantity) + "$";

        binding.detailsFoodName.setText(foodName);
        binding.detailsFoodPrice.setText(foodPrice);
        binding.detailsFoodImage.setImageResource(foodImage);
        binding.quantity.setText(foodQuantity);
        binding.btnBack.setOnClickListener(v -> finish());
        binding.total.setText(Total);


        binding.btnMinus.setOnClickListener(v -> {
            if(quantity > 1) {
                quantity--;

                binding.quantity.setText(String.valueOf(quantity));
                String total = quantity * price + "$";
                binding.total.setText(total);

            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            binding.quantity.setText(String.valueOf(quantity));
            String total = quantity * price + "$";
            binding.total.setText(total);
        });

        binding.btnOrder.setOnClickListener(v -> {
            CartManager.getInstance().addItem(new Item(foodName, foodPrice, foodImage, String.valueOf(quantity)));
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }
}