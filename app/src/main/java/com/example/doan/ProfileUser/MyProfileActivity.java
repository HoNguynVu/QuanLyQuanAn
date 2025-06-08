package com.example.doan.ProfileUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.DatabaseClass.CurrentUser;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPhone, edtbirth;
    private CurrentUser user;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        initViews();
        loadData();
        setupListeners();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtbirth = findViewById(R.id.edtbirth);

        user = CurrentUser.getInstance();
    }

    private void loadData() {
        edtName.setText(user.getName());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhone());
        edtbirth.setText(user.getDateBirth());
    }

    private void setupListeners() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> updateUser());

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(view -> finish());
    }

    private void updateUser() {
        String newName = edtName.getText().toString().trim();
        String newPhone = edtPhone.getText().toString().trim();
        String newDob = edtbirth.getText().toString().trim();

        if (newName.isEmpty() || newPhone.isEmpty() || newDob.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = user.getEmail();

        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy email người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.updateUser(email, newName, newPhone, newDob);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null && response.body().trim().equalsIgnoreCase("success")) {
                    // Cập nhật dữ liệu trong CurrentUser
                    user.setUser(user.getId(), user.getEmail(), newName, newPhone, newDob, user.getRole());

                    Toast.makeText(MyProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // hoặc cập nhật UI
                } else {
                    Toast.makeText(MyProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MyProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
