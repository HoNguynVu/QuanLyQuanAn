package com.example.doan.AdminActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doan.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminSetting extends AppCompatActivity {

    private EditText edtName, edtGender, edtDob;
    private EditText txtPhone, txtEmail;
    private TextView txtChangePassword;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_setting);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.inflateMenu(R.menu.menu_save_setting);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {
                saveUserData();
                return true;
            }
            return false;
        });

        edtName = findViewById(R.id.edtName);
        edtGender = findViewById(R.id.edtGender);
        edtDob = findViewById(R.id.edtDob);
        txtPhone = findViewById(R.id.edtPhone);
        txtEmail = findViewById(R.id.edtEmail);
        txtChangePassword = findViewById(R.id.txtChangePassword);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        txtChangePassword.setOnClickListener(v-> showChangePasswordDialog());

        loadUserData();
    }

    private void loadUserData() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String dob = snapshot.child("dob").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    edtName.setText(name != null ? name : "");
                    edtGender.setText(gender != null ? gender : "");
                    edtDob.setText(dob != null ? dob : "");
                    txtPhone.setText(phone != null ? phone : "");
                    txtEmail.setText(email != null ? email : "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminSetting.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveUserData() {
        String name = edtName.getText().toString().trim();
        String gender = edtGender.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();

        userRef.child("name").setValue(name);
        userRef.child("gender").setValue(gender);
        userRef.child("dob").setValue(dob);
        userRef.child("phone").setValue(phone);
        userRef.child("email").setValue(email);

        Toast.makeText(this, "Đã lưu thay đổi", Toast.LENGTH_SHORT).show();
    }
    private void showChangePasswordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);
        EditText edtOldPassword = view.findViewById(R.id.edtOldPassword);
        EditText edtNewPassword = view.findViewById(R.id.edtNewPassword);
        EditText edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);

        new AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String oldPass = edtOldPassword.getText().toString().trim();
                    String newPass = edtNewPassword.getText().toString().trim();
                    String confirmPass = edtConfirmPassword.getText().toString().trim();

                    // Kiểm tra nhập thiếu
                    if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra độ dài
                    if (newPass.length() < 6) {
                        Toast.makeText(this, "Mật khẩu mới phải từ 6 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra trùng khớp xác nhận
                    if (!newPass.equals(confirmPass)) {
                        Toast.makeText(this, "Xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Tiến hành xác thực lại và đổi mật khẩu
                    reAuthenticateAndChangePassword(oldPass, newPass);
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
    private void reAuthenticateAndChangePassword(String oldPass, String newPass) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || user.getEmail() == null) return;

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);

        user.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    user.updatePassword(newPass)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi đổi mật khẩu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Mật khẩu hiện tại không chính xác", Toast.LENGTH_SHORT).show();
                });
    }

}
