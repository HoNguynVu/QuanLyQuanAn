package com.example.doan.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.AdminFragment.AdminHome;

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
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        Button btn_login = findViewById(R.id.btn_login);
        TextView txt_sign_up = findViewById(R.id.txt_sign_up_nagivate);
        TextView txt_forgot_pass = findViewById(R.id.txt_forgot_password_navigate);

        mAuth = FirebaseAuth.getInstance();

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

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                txt_email.setError("Email không hợp lệ!");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null && user.isEmailVerified()) {
                        String emailValue = user.getEmail();

                        // Gọi API từ PHP server để lấy thông tin user
                        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                        Call<User> call = apiService.getUserInfo(emailValue);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    User u = response.body();
                                    SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", u.name);
                                    editor.putString("email", u.email);
                                    editor.putString("phone", u.phone);
                                    editor.putString("dob", u.date_birth);
                                    editor.putString("role", u.role);
                                    editor.apply();

                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                                    if ("admin".equalsIgnoreCase(u.role)) {
                                        startActivity(new Intent(LoginActivity.this, AdminHome.class));
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Không tìm thấy thông tin người dùng từ máy chủ.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Bạn chưa xác nhận email trong lúc đăng ký. Vui lòng kiểm tra email và xác nhận trước khi đăng nhập.")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                        mAuth.signOut();
                    }
                } else {
                    Exception exception = task.getException();
                    String message;
                    if (exception instanceof FirebaseAuthInvalidUserException) {
                        message = "Tài khoản không tồn tại. Vui lòng kiểm tra lại email!";
                    } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Email hoặc mật khẩu không đúng. Vui lòng thử lại!";
                    } else if (exception instanceof FirebaseAuthException) {
                        message = "Lỗi xác thực: " + exception.getLocalizedMessage();
                    } else {
                        message = "Đăng nhập thất bại: " + exception.getLocalizedMessage();
                    }
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
