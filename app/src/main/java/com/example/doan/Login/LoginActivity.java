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

    private EditText txt_email, txt_password;
    private Button btn_login;
    private TextView txt_sign_up, txt_forgot_pass;
    private ImageView ivTogglePassword;
    private final boolean[] isPassVisible = {false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        CheckLogged();
        initClick();

        btn_login.setOnClickListener(view -> Login());
    }

    public void init()
    {
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);
        txt_sign_up = findViewById(R.id.txt_sign_up_nagivate);
        txt_forgot_pass = findViewById(R.id.txt_forgot_password_navigate);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);

    }

    public void initClick()
    {
        ivTogglePassword.setOnClickListener(v -> {
            if (isPassVisible[0]) {
                txt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
            } else {
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
        btn_login.setOnClickListener(view -> Login());
    }

    public void CheckLogged()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        boolean isLogged = sharedPreferences.getBoolean("is_Logged", false);
        if(isLogged)
        {
            if ("admin".equalsIgnoreCase(sharedPreferences.getString("role", ""))) {
                startActivity(new Intent(LoginActivity.this, AdminHome.class));
            } else {
                startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
            }
            finish();
        }
        else return;
    }

    public void Login() {
        String email = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        btn_login.setEnabled(false);

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<LoginResponse> call = apiService.login(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    btn_login.setEnabled(true);
                    LoginResponse loginResponse = response.body();
                    if ("success".equals(loginResponse.status)) {
                        User u = loginResponse.data;

                        // Lưu vào Singleton
                        CurrentUser.getInstance().setUser(u.id, u.email, u.name, u.phone, u.date_birth, u.role);

                        // SharedPreferences nếu muốn lưu thêm
                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", u.name);
                        editor.putString("email", u.email);
                        editor.putString("phone", u.phone);
                        editor.putString("dob", u.date_birth);
                        editor.putString("role", u.role);
                        editor.putBoolean("is_Logged", true);
                        editor.apply();

                        if ("admin".equalsIgnoreCase(u.role)) {
                            startActivity(new Intent(LoginActivity.this, AdminHome.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
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
                btn_login.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("loi", t.getMessage());
            }
        });

    }
}
