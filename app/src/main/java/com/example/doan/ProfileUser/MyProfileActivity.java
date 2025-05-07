package com.example.doan.ProfileUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        EditText edtName = findViewById(R.id.edtName);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtbirth = findViewById(R.id.edtbirth);

        Intent intent = getIntent();
        String Name = intent.getStringExtra("username");
        String Email = intent.getStringExtra("email");
        String Phone = intent.getStringExtra("phone");
        String Birth = intent.getStringExtra("dob");

        edtName.setText(Name);
        edtEmail.setText(Email);
        edtPhone.setText(Phone);
        edtbirth.setText(Birth);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            // Lấy dữ liệu người dùng nhập
            String newName = edtName.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().trim();
            String newDob = edtbirth.getText().toString().trim();
            // Cập nhật SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", newName);
            editor.putString("phone", newPhone);
            editor.putString("dob", newDob);
            editor.apply();

            // Cập nhật Firebase
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");

                Map<String, Object> updates = new HashMap<>();
                updates.put("name", newName);
                updates.put("phone", newPhone);
                updates.put("dateBirth", newDob);

                database.child(userId).updateChildren(updates)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Lỗi cập nhật Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
            }
        });
    };
}