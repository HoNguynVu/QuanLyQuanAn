package com.example.doan.ProfileUser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(v -> handleChangePassword());
        ImageView imgBack = findViewById(R.id.imgBack1);
        imgBack.setOnClickListener(view -> finish());
    }

    private void handleChangePassword() {
        String currentPassword = edtCurrentPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 9 || !newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{9,}$")) {
            edtNewPassword.setError("Mật khẩu phải có ít nhất 9 ký tự, bao gồm chữ và số");
            return;
        }

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        if (email.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy email người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.changePassword(email, currentPassword, newPassword);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().trim();
                    if (result.equals("success")) {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (result.equals("wrong_password")) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}