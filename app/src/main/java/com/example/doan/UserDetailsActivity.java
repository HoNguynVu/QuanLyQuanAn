package com.example.doan;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.databinding.UserActivityDetailsBinding;

import java.util.Objects;

public class UserDetailsActivity extends AppCompatActivity {
    UserActivityDetailsBinding binding;
    int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int foodID = intent.getIntExtra("MenuItemID", -1);
        String foodName = intent.getStringExtra("MenuItemName");
        double foodPrice = intent.getDoubleExtra("MenuItemPrice", 0);
        String foodQuantity = intent.getStringExtra("MenuItemQuantity");
        String foodImageUrl = intent.getStringExtra("MenuItemImageUrl");
        String foodDescription = intent.getStringExtra("MenuItemDescription");

        quantity = (foodQuantity == null) ? 1 : Integer.parseInt(foodQuantity);
        String Total = String.valueOf(foodPrice * quantity);

        binding.detailsFoodName.setText(foodName);
        binding.detailsFoodPrice.setText(String.valueOf(foodPrice));

        Log.d(TAG, "foodImageUrl: " + foodImageUrl);
        Glide.with(this).load(foodImageUrl).into(binding.detailsFoodImage);
        binding.quantity.setText(String.valueOf(quantity));
        binding.btnBack.setOnClickListener(v -> finish());
        binding.total.setText(Total);
        binding.detailsFoodDescription.setText(foodDescription);

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
            String note = (binding.textInput.getEditText() != null && binding.textInput.getEditText().getText() != null)
                    ? binding.textInput.getEditText().getText().toString()
                    : "";
            Log.d(TAG, "Note: " + note);
            UserCartManager.getInstance().addItem(new FoodItem(foodID, foodName, "", foodPrice, foodImageUrl, 1, "", 5, note,  String.valueOf(quantity)));
            binding.textInput.getEditText().setText("");
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }
}