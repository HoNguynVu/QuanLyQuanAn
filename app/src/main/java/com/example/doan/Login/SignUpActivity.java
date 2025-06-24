package com.example.doan.Login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.DatabaseClassResponse.GenericResponse;
import com.example.doan.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    ImageButton btn_back;
    Button signUpBtn;
    EditText txt_email;
    EditText txt_name;
    EditText txt_phone;
    TextInputEditText txt_date_birth;
    EditText txt_password;
    EditText txt_confirm_password;
    ImageView ivTogglePassword;
    ImageView ivToggleConfirmPassword;

    final boolean[] isPassVisible = {false};
    final boolean[] isConfirmVisible = {false};
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        init();
        initClick();
    }

    public void init()
    {
        signUpBtn = findViewById(R.id.btn_sign_up);

        txt_email = findViewById(R.id.txt_email_sign_up);
        txt_name = findViewById(R.id.txt_name_sign_up);
        txt_phone = findViewById(R.id.txt_phone_sign_up);
        txt_date_birth = findViewById(R.id.txt_date_birth_sign_up);
        txt_password = findViewById(R.id.txt_password_sign_up);
        txt_confirm_password = findViewById(R.id.txt_confirm_password_sign_up);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);
        ivToggleConfirmPassword = findViewById(R.id.iv_toggle_confirm_password);

        btn_back = findViewById(R.id.btn_arrow_back);
    }

    public void initClick()
    {
        ivTogglePassword.setOnClickListener(v -> {
            if (isPassVisible[0]) {
                txt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
            } else {
                txt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
            }
            txt_password.setSelection(txt_password.getText().length());
            isPassVisible[0] = !isPassVisible[0];
        });

        ivToggleConfirmPassword.setOnClickListener(v -> {
            if (isConfirmVisible[0]) {
                txt_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye_closed);
            } else {
                txt_confirm_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye_open);
            }
            txt_confirm_password.setSelection(txt_confirm_password.getText().length());
            isConfirmVisible[0] = !isConfirmVisible[0];
        });

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signUpBtn.setOnClickListener(v ->SignUp());

        txt_date_birth.setInputType(InputType.TYPE_NULL);  // Ngăn bàn phím hiện lên
        txt_date_birth.setFocusable(false);                // Không cho focus textfield

        txt_date_birth.setOnClickListener(v->showDatePicker(txt_date_birth));
    }

    private void showDatePicker(EditText target) {
        calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            target.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void SignUp()
    {
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
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat mysqlFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date parsedDate = inputFormat.parse(dateBirth);
            dateBirth = mysqlFormat.format(parsedDate); // ✅ Gán lại dateBirth theo định dạng MySQL
        } catch (ParseException e) {
            txt_date_birth.setError("Ngày sinh không hợp lệ (dd/MM/yyyy)");
            return;
        }

        if (password.length() < 8 || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            txt_password.setError("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ và số");
            return;
        }

        signUpBtn.setEnabled(false);

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<GenericResponse> call = apiService.register(email, name, password, phone, dateBirth, "User");

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                Log.d("RESPONSE_BODY", new Gson().toJson(response.body()));
                signUpBtn.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse res = response.body();
                    if ("otp_sent".equals(res.status)) {
                        Toast.makeText(SignUpActivity.this, "OTP đã gửi, kiểm tra email!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, OTPVerifyActivity.class);
                        startActivity(intent);
                    } else if ("error".equals(res.status) && res.message.toLowerCase().contains("đã được đăng ký")) {
                        Toast.makeText(SignUpActivity.this, res.message, Toast.LENGTH_SHORT).show();
                        if (res.message.toLowerCase().contains("email")) {
                            txt_email.setError("Email đã được đăng ký");
                        }
                        else if (res.message.toLowerCase().contains("sđt") || res.message.toLowerCase().contains("số điện thoại")) {
                            txt_phone.setError("SĐT đã được đăng ký");
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, res.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                signUpBtn.setEnabled(true);
                Toast.makeText(SignUpActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAGzs", t.getMessage());
            }
        });
    }
}
