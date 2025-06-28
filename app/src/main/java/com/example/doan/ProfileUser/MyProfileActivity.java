package com.example.doan.ProfileUser;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPhone, edtBirth;

    private Button btnSave;

    private ImageView imgBack;
    private CurrentUser currentUser;
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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
        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);
        edtBirth.setInputType(InputType.TYPE_NULL);  // Ngăn bàn phím hiện lên
        edtBirth.setFocusable(false);
    }

    //Load thông tin người dùng vào giao diện
    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        edtName.setText(prefs.getString("username", ""));
        edtEmail.setText(prefs.getString("email", ""));
        edtPhone.setText(prefs.getString("phone", ""));
        try {
            edtBirth.setText(displayFormat.format(serverFormat.parse(prefs.getString("dob", ""))));
        } catch (ParseException e) {
            edtBirth.setText("");
        }
    }

    //Gán sự kiện click cho nút lưu và nút quay lại
    private void setupListeners() {
        btnSave.setOnClickListener(view -> updateUserProfile());
        imgBack.setOnClickListener(view -> finish());
        edtBirth.setOnClickListener(view -> showDatePickerDialog());
    }

    //Kiểm tra đầu vào hợp lệ
    private boolean isInputValid(String name, String phone, String dob) {
        return !name.isEmpty() && !phone.isEmpty() && !dob.isEmpty();
    }

    //Gọi API để cập nhật thông tin người dùng
    private void updateUserProfile() {
        btnSave.setEnabled(false); // Vô hiệu hóa nút lưu trong khi đang xử lý

        String newName = edtName.getText().toString().trim();
        String newPhone = edtPhone.getText().toString().trim();
        String newDob = edtBirth.getText().toString().trim();
        SharedPreferences prefs = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String dobToSend;
        try {
            dobToSend = serverFormat.format(displayFormat.parse(newDob));
        } catch (ParseException e) {
            Toast.makeText(this, "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isInputValid(newName, newPhone, newDob)) {
            showToast("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (email == null || email.isEmpty()) {
            showToast("Không tìm thấy email người dùng!");
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.updateUser(email, newName, newPhone, dobToSend);

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
        btnSave.setEnabled(true); // Kích hoạt lại nút lưu sau khi hoàn thành
    }

    //Hiển thị Toast thông báo
    private void showToast(String message) {
        Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            edtBirth.setText(displayFormat.format(selected.getTime()));
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }
}
