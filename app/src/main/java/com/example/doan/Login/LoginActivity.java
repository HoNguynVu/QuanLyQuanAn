package com.example.doan.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.AdminFragment.AdminHome;
import com.example.doan.DatabaseClassResponse.LoginResponse;
import com.example.doan.DatabaseClass.User;
import com.example.doan.DatabaseClass.CurrentUser;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import com.example.doan.UserActivity.UserMainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtSignUp, txtForgotPassword;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        edtEmail = findViewById(R.id.txt_email);
        edtPassword = findViewById(R.id.txt_password);
        btnLogin = findViewById(R.id.btn_login);
        txtSignUp = findViewById(R.id.txt_sign_up_nagivate);
        txtForgotPassword = findViewById(R.id.txt_forgot_password_navigate);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(view -> loginUser());

        txtSignUp.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        txtForgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
            finish();
        });

        ivTogglePassword.setOnClickListener(view -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
        } else {
            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
        }
        edtPassword.setSelection(edtPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<LoginResponse> call = apiService.login(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if ("success".equalsIgnoreCase(loginResponse.status)) {
                        User user = loginResponse.data;

                        // Gán vào Singleton
                        CurrentUser.getInstance().setUser(
                                user.id,
                                user.email,
                                user.name,
                                user.phone,
                                user.date_birth,
                                user.role
                        );

                        // Ghi thêm vào SharedPreferences nếu cần dùng sau này
                        saveUserToPreferences(user);

                        // Điều hướng theo vai trò
                        navigateToHome(user.role);
                    } else {
                        showToast(loginResponse.message);
                    }
                } else {
                    showToast("Lỗi phản hồi từ server");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showToast("Lỗi kết nối: " + t.getMessage());
                Log.e("LoginError", t.getMessage());
            }
        });
    }

    private void saveUserToPreferences(User user) {
        SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", user.email);
        editor.putString("username", user.name);
        editor.putString("phone", user.phone);
        editor.putString("dob", user.date_birth);
        editor.putString("role", user.role);
        editor.apply();
    }

    private void navigateToHome(String role) {
        Intent intent = "admin".equalsIgnoreCase(role)
                ? new Intent(this, AdminHome.class)
                : new Intent(this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
