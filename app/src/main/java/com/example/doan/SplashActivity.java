package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        Button btn_sign_up = findViewById(R.id.btn_sign_up_navigate);
        btn_sign_up.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        Button btn_login = findViewById(R.id.btn_login_navigate);
        btn_login.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}