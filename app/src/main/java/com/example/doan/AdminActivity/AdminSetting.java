package com.example.doan.AdminActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doan.DatabaseClass.CurrentUser;
import com.example.doan.DatabaseClassResponse.GenericResponse;
import com.example.doan.DatabaseClass.User;
import com.example.doan.DatabaseClassResponse.UserResponse;
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
    private Toolbar toolbar;
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_setting);

        initViews();
        initClick();
        loadUserData();
    }

    public void initViews()
    {
        toolbar = findViewById(R.id.topAppBar);
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

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        currentEmail = sharedPreferences.getString("email", null);
        if (currentEmail == null) {
            Toast.makeText(this, "Không có email người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }

        edtDob.setInputType(InputType.TYPE_NULL);  // Ngăn bàn phím hiện lên
        edtDob.setFocusable(false);
    }

    public void initClick()
    {
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
                    if (user != null && user.getEmail() != null) {
                        edtName.setText(user.getName());
                        edtPhone.setText(user.getPhone());
                        edtEmail.setText(user.getEmail());

                        if (user.getDate_birth() != null && !user.getDate_birth().isEmpty()) {
                            try {
                                edtDob.setText(displayFormat.format(serverFormat.parse(user.getDate_birth())));
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
                    SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", name);
                    editor.putString("phone", phone);
                    editor.putString("dob", dob);
                    editor.apply();
                    finish();
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
        // Tạo dialog tùy chỉnh
        Dialog dialog = new Dialog(this, R.style.FullWidthDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);
        dialog.setContentView(view);
        
        // Thiết lập kích thước của dialog để hiển thị toàn màn hình chiều ngang
        dialog.getWindow().setLayout(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT
        );
        
        // Thiết lập các trường nhập liệu
        EditText edtOldPassword = view.findViewById(R.id.edtOldPassword);
        EditText edtNewPassword = view.findViewById(R.id.edtNewPassword);
        EditText edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        
        // Thiết lập các nút
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        
        // Xử lý sự kiện nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        // Xử lý sự kiện nút Lưu
        btnConfirm.setOnClickListener(v -> {
            String oldPass = edtOldPassword.getText().toString().trim();
            String newPass = edtNewPassword.getText().toString().trim();
            String confirmPass = edtConfirmPassword.getText().toString().trim();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                edtConfirmPassword.setError("Mật khẩu xác nhận không khớp");
                return;
            }

            if (newPass.length() < 8 || !newPass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                edtNewPassword.setError("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ và số");
                return;
            }

            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            apiService.changePassword(currentEmail, oldPass, newPass).enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(AdminSetting.this, response.body().message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(AdminSetting.this, "Lỗi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    Toast.makeText(AdminSetting.this, "Lỗi server", Toast.LENGTH_SHORT).show();
                }
            });
        });
        
        // Hiển thị dialog
        dialog.show();
    }
}
