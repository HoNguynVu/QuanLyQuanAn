package com.example.doan.AdminActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.doan.DatabaseClass.Discount;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import retrofit2.*;

public class EditDiscountActivity extends AppCompatActivity {

    private TextInputEditText edtCode, edtPercent, edtMaxAmount, edtFrom, edtTo;
    private SwitchMaterial switchActive;
    private MaterialButton btnSave;
    private APIService apiService;
    private Discount discount;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_discount);

        setupToolbar();
        initViews();

        apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        if (getIntent().hasExtra("discount")) {
            discount = (Discount) getIntent().getSerializableExtra("discount");
            populateData();
        }

        edtFrom.setOnClickListener(v -> showDatePicker(edtFrom));
        edtTo.setOnClickListener(v -> showDatePicker(edtTo));

        btnSave.setOnClickListener(v -> {
            if (!validateInput()) return;
            if (discount == null) {
                createDiscount();
            } else {
                updateDiscount();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initViews() {
        edtCode = findViewById(R.id.edtCode);
        edtPercent = findViewById(R.id.edtPercent);
        edtMaxAmount = findViewById(R.id.edtMaxAmount);
        edtFrom = findViewById(R.id.edtFrom);
        edtTo = findViewById(R.id.edtTo);
        switchActive = findViewById(R.id.switchActive);
        btnSave = findViewById(R.id.btnSave);
    }

    private void populateData() {
        edtCode.setText(discount.getCode());
        edtPercent.setText(String.valueOf(discount.getDiscountPercent()));
        edtMaxAmount.setText(String.valueOf(discount.getMaxDiscountAmount()));
        edtFrom.setText(sdf.format(discount.getValidFrom()));
        edtTo.setText(sdf.format(discount.getValidTo()));
        switchActive.setChecked(discount.isActive());
    }

    private void showDatePicker(TextInputEditText target) {
        calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            target.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private boolean validateInput() {
        if (edtCode.getText().toString().trim().isEmpty() ||
                edtPercent.getText().toString().trim().isEmpty() ||
                edtMaxAmount.getText().toString().trim().isEmpty() ||
                edtFrom.getText().toString().trim().isEmpty() ||
                edtTo.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createDiscount() {
        Discount d = new Discount();
        d.setCode(edtCode.getText().toString().trim());
        d.setDiscountPercent(Integer.parseInt(edtPercent.getText().toString().trim()));
        d.setMaxDiscountAmount(Double.parseDouble(edtMaxAmount.getText().toString().trim()));


        try {
            d.setValidFrom(sdf.parse(edtFrom.getText().toString().trim()));
            d.setValidTo(sdf.parse(edtTo.getText().toString().trim()));
        } catch (Exception e) {
            Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        d.setActive(switchActive.isChecked());

        apiService.createDiscount(d).enqueue(new Callback<Discount>() {
            @Override
            public void onResponse(Call<Discount> call, Response<Discount> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditDiscountActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditDiscountActivity.this, "Lỗi thêm dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Discount> call, Throwable t) {
                Toast.makeText(EditDiscountActivity.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateDiscount() {
        discount.setCode(edtCode.getText().toString().trim());
        discount.setDiscountPercent(Integer.parseInt(edtPercent.getText().toString().trim()));
        discount.setMaxDiscountAmount(Double.parseDouble(edtMaxAmount.getText().toString().trim()));
        try {
            discount.setValidFrom(sdf.parse(edtFrom.getText().toString().trim()));
            discount.setValidTo(sdf.parse(edtTo.getText().toString().trim()));
        } catch (Exception e) {
            Toast.makeText(this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        discount.setActive(switchActive.isChecked());

        apiService.updateDiscount(discount.getId(), discount).enqueue(new Callback<Discount>() {
            @Override
            public void onResponse(Call<Discount> call, Response<Discount> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditDiscountActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditDiscountActivity.this, "Lỗi cập nhật dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Discount> call, Throwable t) {
                Toast.makeText(EditDiscountActivity.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
