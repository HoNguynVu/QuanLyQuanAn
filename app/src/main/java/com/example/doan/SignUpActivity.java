package com.example.doan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            sdf.setLenient(false); // không chấp nhận ngày không hợp lệ

            try {
                Date parsedDate = sdf.parse(dateBirth); // thử parse ngày
            } catch (ParseException e) {
                txt_date_birth.setError("Ngày sinh không đúng định dạng dd/MM/yy");
                return;
            }

            if (password.length() < 9 || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{9,}$")) {
                txt_password.setError("Mật khẩu phải có ít nhất 9 ký tự, bao gồm chữ và số");
                return;
            }
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            // Gửi email xác nhận
                            firebaseUser.sendEmailVerification()
                                    .addOnSuccessListener(unused -> {
                                        // Lưu thông tin user vào Database
                                        String uid = firebaseUser.getUid();
                                        User user = new User(email, name, phone, dateBirth, password);

                                        mDatabase.child(uid).setValue(user)
                                                .addOnSuccessListener(dataSnapshot -> {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                                    builder.setMessage("Chúng tôi đã gửi yêu cầu xác nhận email đến email của bạn. Vui lòng kiểm tra email để xác thực email của bạn.")
                                                            .setCancelable(false)
                                                            .setPositiveButton("OK", (dialog, id) -> {
                                                                // Đóng dialog khi người dùng nhấn OK
                                                                dialog.dismiss();

                                                                // Đăng xuất người dùng sau khi gửi email xác nhận
                                                                mAuth.signOut();

                                                                // Chuyển về màn hình Login
                                                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                                finish();  // Đảm bảo màn hình SignUp không còn trên stack
                                                            });

                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(this, "Lỗi lưu thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Không thể gửi email xác nhận: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });

    }
}