package com.example.doan.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        EditText txt_email = findViewById(R.id.txt_email_forgot_password);
        Button btn_lg = findViewById(R.id.btn_fgpass);

        ImageButton btn_back = findViewById(R.id.btn_arrow_back_FG);
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        btn_lg.setOnClickListener(v -> {
            String email = txt_email.getText().toString().trim();

            if (email.isEmpty()) {
                txt_email.setError("Vui lòng nhập email đã đăng ký");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gửi yêu cầu thay đổi mật khẩu qua email
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("Đã gửi yêu cầu thay đổi mật khẩu đến email của bạn . Vui lòng truy cập email của bạn để thay đổi mật khẩu!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", (dialog, id) -> {
                                        dialog.dismiss();
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                            finish();
                        } else {
                            // Nếu có lỗi xảy ra
                            Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}