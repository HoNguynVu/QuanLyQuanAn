package com.example.doan.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.AdminFragment.AdminHome;

import com.example.doan.DatabaseClass.LoginResponse;
import com.example.doan.DatabaseClass.User;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText txt_email, txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        Button btn_login = findViewById(R.id.btn_login);
        TextView txt_sign_up = findViewById(R.id.txt_sign_up_nagivate);
        TextView txt_forgot_pass = findViewById(R.id.txt_forgot_password_navigate);
        ImageView ivTogglePassword = findViewById(R.id.iv_toggle_password);
        final boolean[] isPassVisible = {false};
        ivTogglePassword.setOnClickListener(v -> {
            if (isPassVisible[0])
            {
                txt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
            }
            else
            {
                txt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
            }
            txt_password.setSelection(txt_password.getText().length());
            isPassVisible[0] = !isPassVisible[0];
        });


        txt_sign_up.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        txt_forgot_pass.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });

        btn_login.setOnClickListener(view -> {
            String email = txt_email.getText().toString().trim();
            String password = txt_password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<LoginResponse> call = apiService.login(email, password);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        if ("success".equals(loginResponse.status)) {
                            User u = loginResponse.data;

                            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", u.name);
                            editor.putString("email", u.email);
                            editor.putString("phone", u.phone);
                            editor.putString("dob", u.date_birth);
                            editor.putString("role", u.role);
                            editor.apply();

                            if ("admin".equalsIgnoreCase(u.role)) {
                                startActivity(new Intent(LoginActivity.this, AdminHome.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, loginResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi phản hồi từ server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("loi", t.getMessage() );
                }
            });
        });

    }
}
