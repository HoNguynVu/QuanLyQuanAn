package com.example.doan.ProfileUser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.DatabaseClassResponse.GenericResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;

    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        initViews();
        setupListeners();
    }

    // Ánh xạ các view trong layout
    private void initViews() {
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        imgBack = findViewById(R.id.imgBack1);
    }

    // Gắn sự kiện cho các thành phần giao diện
    private void setupListeners() {
        btnChangePassword.setOnClickListener(view -> handleChangePassword());

        imgBack.setOnClickListener(view -> finish());
    }

    // Xử lý logic đổi mật khẩu khi người dùng nhấn nút "Đổi mật khẩu"
    private void handleChangePassword() {
        btnChangePassword.setEnabled(false);

        String oldPassword = edtCurrentPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (!validateInput(oldPassword, newPassword, confirmPassword)) return;

        String email = getUserEmail();
        if (email.isEmpty()) {
            showToast("Không tìm thấy email người dùng");
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<GenericResponse> call = apiService.changePassword(email, oldPassword, newPassword);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleServerResponse(response.body());
                } else {
                    showToast("Lỗi server: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                showToast("Lỗi kết nối: " + t.getMessage());
            }
        });

        btnChangePassword.setEnabled(true);
    }


    // Kiểm tra dữ liệu đầu vào (mật khẩu cũ, mới, xác nhận)
    private boolean validateInput(String current, String newPass, String confirm) {
        if (TextUtils.isEmpty(current) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirm)) {
            showToast("Vui lòng nhập đầy đủ thông tin");
            return false;
        }

        if (!newPass.equals(confirm)) {
            showToast("Mật khẩu mới không khớp");
            return false;
        }

        if (newPass.length() < 9 || !newPass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{9,}$")) {
            edtNewPassword.setError("Mật khẩu phải có ít nhất 9 ký tự, bao gồm chữ và số");
            return false;
        }

        return true;
    }

    // Xử lý kết quả trả về từ API sau khi đổi mật khẩu
    private void handleServerResponse(GenericResponse result) {
        switch (result.status.toLowerCase()) {
            case "success":
                showToast(result.message);
                finish();
                break;
            case "wrong_password":
                showToast("Mật khẩu hiện tại không đúng");
                break;
            default:
                showToast(result.message);
                break;
        }
    }

    // Lấy email người dùng từ SharedPreferences
    private String getUserEmail() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return prefs.getString("email", "");
    }

    // Hiển thị thông báo dạng Toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
