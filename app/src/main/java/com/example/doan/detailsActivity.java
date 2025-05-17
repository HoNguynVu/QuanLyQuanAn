package com.example.doan;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.databinding.ActivityDetailsBinding;

public class detailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;

    public detailsActivity(ActivityDetailsBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String foodName = intent.getStringExtra("MenuItemName");
        int foodImage = intent.getIntExtra("MenuItemImage", 0);

        binding.detailsFoodName.setText(foodName);
        binding.detailsFoodImage.setImageResource(foodImage);
    }
}