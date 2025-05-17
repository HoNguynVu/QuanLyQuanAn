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

import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPhone, edtbirth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        initViews();
        loadIntentData();
        setupListeners();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtbirth = findViewById(R.id.edtbirth);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
    }

    private void loadIntentData() {
        Intent intent = getIntent();
        edtName.setText(intent.getStringExtra("username"));
        edtEmail.setText(intent.getStringExtra("email"));
        edtPhone.setText(intent.getStringExtra("phone"));
        edtbirth.setText(intent.getStringExtra("dob"));
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
        String email = sharedPreferences.getString("email", null);

        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy email người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.updateUser(email, newName, newPhone, newDob);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && "success".equalsIgnoreCase(response.body().trim())) {
                    // Lưu lại SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", newName);
                    editor.putString("phone", newPhone);
                    editor.putString("dob", newDob);
                    editor.apply();

                    Toast.makeText(MyProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // để ProfileFragment biết reload
                    finish();
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
