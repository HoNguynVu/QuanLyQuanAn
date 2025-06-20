package com.example.doan.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.Network.APIService;
import com.example.doan.DatabaseClassResponse.GenericResponse;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerifyActivity extends AppCompatActivity {

    private EditText edtOtp;
    private Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        init();
        initClick();
    }

    public void init()
    {
        edtOtp = findViewById(R.id.edt_otp);
        btnVerify = findViewById(R.id.btn_verify_otp);
    }

    public void initClick()
    {
        btnVerify.setOnClickListener(v -> verifyOtp());
    }

    public void verifyOtp()
    {
        String otp = edtOtp.getText().toString().trim();

        if (otp.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        btnVerify.setEnabled(false);

        APIService api = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<GenericResponse> call = api.verifyOtp(otp);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                btnVerify.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse res = response.body();
                    Toast.makeText(OTPVerifyActivity.this, res.message, Toast.LENGTH_SHORT).show();
                    if ("success".equals(res.status)) {
                        startActivity(new Intent(OTPVerifyActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(OTPVerifyActivity.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                btnVerify.setEnabled(true);
                Toast.makeText(OTPVerifyActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
