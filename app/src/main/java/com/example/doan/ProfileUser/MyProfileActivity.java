package com.example.doan.ProfileUser;

import android.content.Context;
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

    private EditText edtName, edtEmail, edtPhone, edtBirth;
    private CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        initViews();
        loadUserData();
        setupListeners();
    }

    //Ánh xạ các view và lấy thông tin người dùng hiện tại
    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirth = findViewById(R.id.edtBirth);
        currentUser = CurrentUser.getInstance();
    }

    //Load thông tin người dùng vào giao diện
    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        edtName.setText(prefs.getString("username", ""));
        edtEmail.setText(prefs.getString("email", ""));
        edtPhone.setText(prefs.getString("phone", ""));
        edtBirth.setText(prefs.getString("dob", ""));
    }

    //Gán sự kiện click cho nút lưu và nút quay lại
    private void setupListeners() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> updateUserProfile());

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(view -> finish());
    }

    //Kiểm tra đầu vào hợp lệ
    private boolean isInputValid(String name, String phone, String dob) {
        return !name.isEmpty() && !phone.isEmpty() && !dob.isEmpty();
    }

    //Gọi API để cập nhật thông tin người dùng
    private void updateUserProfile() {
        String newName = edtName.getText().toString().trim();
        String newPhone = edtPhone.getText().toString().trim();
        String newDob = edtBirth.getText().toString().trim();
        SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");

        if (!isInputValid(newName, newPhone, newDob)) {
            showToast("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (email == null || email.isEmpty()) {
            showToast("Không tìm thấy email người dùng!");
            return;
        }


        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.updateUser(email, newName, newPhone, newDob);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null ) {
                    String result = response.body().trim();

                    switch (result) {
                        case "phone_exists":
                            showToast("Số điện thoại đã được sử dụng bởi người dùng khác!");
                            break;
                        case "success":
                            // Lưu thông tin mới vào SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("username", newName);
                            editor.putString("phone", newPhone);
                            editor.putString("dob", newDob);
                            editor.apply();

                            showToast("Cập nhật thành công!");
                            finish();
                            break;
                        case "error":
                        default:
                            showToast("Cập nhật thất bại! Vui lòng thử lại.");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast("Lỗi: " + t.getMessage());
            }
        });
    }

    //Hiển thị Toast thông báo
    private void showToast(String message) {
        Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
