package com.example.doan;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.databinding.ActivityDetailsBinding;

public class detailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;
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
        int foodImage = intent.getIntExtra("MenuItemImage", 0);

        binding.detailsFoodName.setText(foodName);
        binding.detailsFoodImage.setImageResource(foodImage);
        binding.btnBack.setOnClickListener(v -> finish());
    }
}