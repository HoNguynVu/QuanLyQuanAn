package com.example.doan.UserActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.R;
import com.example.doan.UserFragment.UserHomeFragment;
import com.example.doan.databinding.UserActivityOrderSuccessBinding;

public class UserOrderSuccessActivity extends AppCompatActivity {
    private UserActivityOrderSuccessBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityOrderSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBtnBackToHome();
    }

    public void setBtnBackToHome() {
        binding.backToHome.setOnClickListener(v -> {
            binding.backToHome.setEnabled(false);
            Intent intent = new Intent(this, UserMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // Bật lại sau 500ms
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.backToHome.setEnabled(true);
            }, 500);
            finish();
        });
    }
}