package com.example.doan.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.DatabaseClass.GenericResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText txtEmail;
    Button btnSendOtp;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        txtEmail = findViewById(R.id.txt_email_forgot_password);
        btnSendOtp = findViewById(R.id.btn_fgpass);
        btnBack = findViewById(R.id.btn_arrow_back_FG);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });

        btnSendOtp.setOnClickListener(v -> {
            String email = txtEmail.getText().toString().trim();

            if (email.isEmpty()) {
                txtEmail.setError("Vui lòng nhập email đã đăng ký");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                txtEmail.setError("Email không hợp lệ");
                return;
            }

            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<GenericResponse> call = apiService.sendResetOtp(email);

            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        GenericResponse res = response.body();
                        if ("otp_sent".equals(res.status)) {
                            Toast.makeText(ForgotPasswordActivity.this, "Mã OTP đã được gửi!", Toast.LENGTH_SHORT).show();

                            // Chuyển sang màn hình nhập OTP
                            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyResetOtpActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, res.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
