package com.example.doan.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;
import com.example.doan.Adapter.DiscountAdapter;
import com.example.doan.DatabaseClass.Discount;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import java.util.*;
import retrofit2.*;

public class ManageDiscountActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiscountAdapter adapter;
    private List<Discount> discountList = new ArrayList<>();
    private APIService apiService;
    private Toolbar toolbar;

    private final ActivityResultLauncher<Intent> discountLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadDiscounts();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_discount);

        SetupToolbar();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        adapter = new DiscountAdapter(discountList, new DiscountAdapter.OnDiscountListener() {
            @Override
            public void onEdit(Discount discount) {
                Intent intent = new Intent(ManageDiscountActivity.this, EditDiscountActivity.class);
                intent.putExtra("discount", discount);
                discountLauncher.launch(intent);
            }

            @Override
            public void onDelete(Discount discount) {
                deleteDiscount(discount.getId());
            }
        });

        recyclerView.setAdapter(adapter);
        loadDiscounts();
    }

    private void SetupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);// Icon hình X
        }
        // Đặt text khi nhấn giữ nút X
        toolbar.setNavigationContentDescription("Thoát");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discount, menu);
        menu.findItem(R.id.action_add_discount).setTitle("Thêm mã giảm giá");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Đóng Activity khi bấm X
            return true;
        }
        if (item.getItemId() == R.id.action_add_discount) {
            discountLauncher.launch(new Intent(this, EditDiscountActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDiscounts() {
        apiService.getAllDiscounts().enqueue(new Callback<List<Discount>>() {
            @Override
            public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    discountList.clear();
                    discountList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageDiscountActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Discount>> call, Throwable t) {
                Toast.makeText(ManageDiscountActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDiscount(int id) {
        apiService.deleteDiscount(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageDiscountActivity.this, "Đã xóa mã giảm giá", Toast.LENGTH_SHORT).show();
                    loadDiscounts();
                } else {
                    Toast.makeText(ManageDiscountActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageDiscountActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
