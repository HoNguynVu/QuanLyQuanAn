package com.example.doan.UserActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_activity_order_success);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        UserActivityOrderSuccessBinding binding = UserActivityOrderSuccessBinding.inflate(getLayoutInflater());
        binding.backToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserHomeFragment.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }
}