package com.example.doan.AdminActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doan.DatabaseClass.CurrentUser;
import com.example.doan.DatabaseClass.GenericResponse;
import com.example.doan.DatabaseClass.User;
import com.example.doan.DatabaseClass.UserResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSetting extends AppCompatActivity {

    private EditText edtName, edtPhone, edtEmail, edtDob;
    private TextView txtChangePassword;
    private String currentEmail;

    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_setting);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.menu_save_setting);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {
                saveUserData();
                return true;
            }
            return false;
        });

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtDob = findViewById(R.id.edtDob);
        txtChangePassword = findViewById(R.id.txtChangePassword);

        // DatePicker cho ngày sinh
        edtDob.setOnClickListener(v -> showDatePickerDialog());

        currentEmail = CurrentUser.getInstance().getEmail();
        if (currentEmail == null) {
            Toast.makeText(this, "Không có email người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }

        loadUserData();
        txtChangePassword.setOnClickListener(v -> showChangePasswordDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            edtDob.setText(displayFormat.format(selected.getTime()));
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private void loadUserData() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        apiService.getAdminInfo(currentEmail).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body().user;
                    if (user != null && user.email != null) {
                        edtName.setText(user.name);
                        edtPhone.setText(user.phone);
                        edtEmail.setText(user.email);

                        if (user.date_birth != null && !user.date_birth.isEmpty()) {
                            try {
                                edtDob.setText(displayFormat.format(serverFormat.parse(user.date_birth)));
                            } catch (ParseException e) {
                                edtDob.setText("");
                            }
                        }
                    } else {
                        Toast.makeText(AdminSetting.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminSetting.this, "Phản hồi không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(AdminSetting.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();

        String dobToSend;
        try {
            dobToSend = serverFormat.format(displayFormat.parse(dob));
        } catch (ParseException e) {
            Toast.makeText(this, "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        apiService.updateAdminInfo(currentEmail, name, phone, dobToSend).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AdminSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminSetting.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(AdminSetting.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
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

                    if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!newPass.equals(confirmPass)) {
                        Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                    apiService.changePassword(currentEmail, oldPass, newPass).enqueue(new Callback<GenericResponse>() {
                        @Override
                        public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AdminSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdminSetting.this, "Lỗi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GenericResponse> call, Throwable t) {
                            Toast.makeText(AdminSetting.this, "Lỗi server", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
    @Override
    public void onBackPressed() {
        // Quay lại fragment trước (AdminProfileFragment)
        super.onBackPressed(); // tự động quay lại Fragment trước
    }

}
