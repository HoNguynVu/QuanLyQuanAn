package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        TextView txt_forgot_pass = findViewById(R.id.txt_forgot_password_navigate);

        EditText txt_email = findViewById(R.id.txt_email);
        EditText txt_password = findViewById(R.id.txt_password);

        txt_forgot_pass.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> {
            String email = txt_email.getText().toString();
            String password = txt_password.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                txt_email.setError("Email không hợp lệ!");
                return;
            }
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // Hiển thị dialog yêu cầu người dùng xác nhận email
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("Bạn chưa xác nhận email trong lúc đăng ký. Vui lòng kiểm tra email và xác nhận trước khi đăng nhập.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", (dialog, id) -> {
                                            dialog.dismiss();
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                                mAuth.signOut(); // Đăng xuất lại
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