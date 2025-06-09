package com.example.doan.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

public class ResetPasswordActivity extends AppCompatActivity {

    EditText edtNewPassword, edtConfirmPassword;
    Button btnReset;
    String email;
    ImageView ivTogglePassword;
    ImageView ivToggleConfirmPassword;
    final boolean[] showNew = {false};
    final boolean[] showConfirm = {false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        init();
        initClick();
    }

    public void init()
    {
        edtNewPassword = findViewById(R.id.edt_new_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnReset = findViewById(R.id.btn_reset_password);
        ivTogglePassword = findViewById(R.id.iv_toggle_new_pass);
        ivToggleConfirmPassword = findViewById(R.id.iv_toggle_confirm_pass);
        email = getIntent().getStringExtra("email");
    }

    public void initClick()
    {
        ivTogglePassword.setOnClickListener(v ->
        {
            if (showNew[0])
            {
                edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
            }
            else {
                edtNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
            }
            edtNewPassword.setSelection(edtNewPassword.getText().length());
            showNew[0] = !showNew[0];
        });

        ivToggleConfirmPassword.setOnClickListener(v1->
        {
            if(showConfirm[0])
            {
                edtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye_closed);
            }
            else
            {
                edtConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye_open);
            }
            edtConfirmPassword.setSelection(edtConfirmPassword.getText().length());
            showConfirm[0] = !showConfirm[0];
        });

        btnReset.setOnClickListener(v -> resetPassword());
    }

    public void resetPassword()
    {
        String pass = edtNewPassword.getText().toString().trim();
        String confirm = edtConfirmPassword.getText().toString().trim();

        if (pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            edtConfirmPassword.setError("Mật khẩu không khớp");
            return;
        }

        if (pass.length() < 8 || !pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{9,}$")) {
            edtNewPassword.setError("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ và số");
            return;
        }
        btnReset.setEnabled(false);

        APIService api = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<GenericResponse> call = api.resetPassword(email, pass);

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                btnReset.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse res = response.body();
                    Toast.makeText(ResetPasswordActivity.this, res.message, Toast.LENGTH_SHORT).show();

                    if ("success".equals(res.status)) {
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Lỗi từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                btnReset.setEnabled(true);
                Toast.makeText(ResetPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
