package com.example.doan.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.DatabaseClass.GenericResponse;
import com.example.doan.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        Button signUpBtn = findViewById(R.id.btn_sign_up);

        EditText txt_email = findViewById(R.id.txt_email_sign_up);
        EditText txt_name = findViewById(R.id.txt_name_sign_up);
        EditText txt_phone = findViewById(R.id.txt_phone_sign_up);
        EditText txt_date_birth = findViewById(R.id.txt_date_birth_sign_up);
        EditText txt_password = findViewById(R.id.txt_password_sign_up);
        EditText txt_confirm_password = findViewById(R.id.txt_confirm_password_sign_up);

        ImageButton btn_back = findViewById(R.id.btn_arrow_back);
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signUpBtn.setOnClickListener(v -> {
            String email = txt_email.getText().toString().trim();
            String name = txt_name.getText().toString().trim();
            String phone = txt_phone.getText().toString().trim();
            String dateBirth = txt_date_birth.getText().toString().trim();
            String password = txt_password.getText().toString().trim();
            String confirmPassword = txt_confirm_password.getText().toString().trim();

            if (email.isEmpty() || name.isEmpty() || phone.isEmpty() || dateBirth.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                txt_email.setError("Email không hợp lệ!");
                return;
            }
            if (!password.equals(confirmPassword)) {
                txt_confirm_password.setError("Mật khẩu xác nhận không khớp");
                return;
            }
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            SimpleDateFormat mysqlFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date parsedDate = inputFormat.parse(dateBirth);
                dateBirth = mysqlFormat.format(parsedDate); // ✅ Gán lại dateBirth theo định dạng MySQL
            } catch (ParseException e) {
                txt_date_birth.setError("Ngày sinh không hợp lệ (dd/MM/yy)");
                return;
            }


            if (password.length() < 9 || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{9,}$")) {
                txt_password.setError("Mật khẩu phải có ít nhất 9 ký tự, bao gồm chữ và số");
                return;
            }

            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<GenericResponse> call = apiService.register(email, name, password, phone, dateBirth, "User");

            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    Log.d("RESPONSE_BODY", new Gson().toJson(response.body()));

                    if (response.isSuccessful() && response.body() != null) {
                        GenericResponse res = response.body();
                        if ("otp_sent".equals(res.status)) {
                            Toast.makeText(SignUpActivity.this, "OTP đã gửi, kiểm tra email!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, OTPVerifyActivity.class);
                            startActivity(intent);
                        } else if ("error".equals(res.status) && res.message.toLowerCase().contains("đã tồn tại")) {
                            Toast.makeText(SignUpActivity.this, res.message, Toast.LENGTH_SHORT).show();
                            if (res.message.toLowerCase().contains("email")) {
                                txt_email.setError("Email đã được đăng ký");
                            }
                            if (res.message.toLowerCase().contains("sđt") || res.message.toLowerCase().contains("số điện thoại")) {
                                txt_phone.setError("SĐT đã được đăng ký");
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, res.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAGzs", t.getMessage());
                }
            });
        });
    }
}
