package com.example.doan.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class VerifyResetOtpActivity extends AppCompatActivity {

    EditText edtOtp;
    Button btnVerify;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_reset_otp);

        edtOtp = findViewById(R.id.edt_otp);
        btnVerify = findViewById(R.id.btn_verify_otp);

        email = getIntent().getStringExtra("email");

        btnVerify.setOnClickListener(v -> verifyOtp());
    }
    public void verifyOtp()
    {
        String otp = edtOtp.getText().toString().trim();

        if (otp.isEmpty()) {
            edtOtp.setError("Vui lòng nhập mã OTP");
            return;
        }

        APIService api = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<GenericResponse> call = api.verifyResetOtp(email, otp);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse res = response.body();
                    Toast.makeText(VerifyResetOtpActivity.this, res.message, Toast.LENGTH_SHORT).show();

                    if ("success".equals(res.status)) {
                        Intent intent = new Intent(VerifyResetOtpActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(VerifyResetOtpActivity.this, "Lỗi xác minh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(VerifyResetOtpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
