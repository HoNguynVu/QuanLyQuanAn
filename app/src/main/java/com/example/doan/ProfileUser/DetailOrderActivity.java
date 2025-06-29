package com.example.doan.ProfileUser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.Adapter.OrderItemAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.OrderItem;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderActivity extends AppCompatActivity {

    private TextView txtHeader, txtOrderId, txtDate, txtStatus, txtTotal,txtDiscount,txtAddress;
    private ListView listView;
    private List<OrderItemWithFood> itemList = new ArrayList<>();
    private OrderItemAdapter adapter;
    private Button btnBack,btnAddReview;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        initViews();
        setupBackButton();
        setupAddReviewButton();
        orderId = getIntent().getStringExtra("order_id");
        if (orderId == null || orderId.isEmpty()) {
            showToast("Không có mã đơn hàng");
            finish();
            return;
        }
        loadOrderDetails(orderId);
        updateUIFromIntent();
    }

    // Khởi tạo view
    private void initViews() {
        txtHeader = findViewById(R.id.txt_header);
        txtOrderId = findViewById(R.id.txt_order_id);
        txtDiscount = findViewById(R.id.txt_discount);
        txtDate = findViewById(R.id.txt_order_date);
        txtStatus = findViewById(R.id.txt_order_status);
        txtAddress = findViewById(R.id.txt_order_address);
        txtTotal = findViewById(R.id.txt_total_amount);

        listView = findViewById(R.id.lv_order_items);
        btnBack = findViewById(R.id.btn_back);
        btnAddReview = findViewById(R.id.btn_Add_Review);

    }

    // Thiết lập sự kiện nút Back
    private void setupBackButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    // Thiết lập sự kiện nút Thêm đánh giá
    private void setupAddReviewButton() {
        btnAddReview.setOnClickListener(v -> {
            btnAddReview.setEnabled(false);
           showToast("Hãy chọn món ăn bạn muốn đánh giá");
           adapter.setReviewMode(true);
           if (adapter != null) {
                adapter.setReviewMode(true);
                adapter.notifyDataSetChanged(); // Cập nhật lại để click hoạt động
           }
            btnAddReview.setEnabled(true);
        });
    }

    // Gọi API lấy chi tiết đơn hàng (list món)
    private void loadOrderDetails(String orderId) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        Log.d("API", "Gửi yêu cầu lấy đơn hàng với orderId = " + orderId);

        apiService.getOrderItems(orderId).enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleOrderItemsResponse(response.body());
                } else {
                    handleOrderItemsError(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                handleOrderItemsFailure(t);
            }
        });
    }

    // Xử lý dữ liệu món ăn lấy về từ API đơn hàng
    private void handleOrderItemsResponse(List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            showToast("Đơn hàng không có món nào");
            return;
        }

        itemList.clear();

        final int totalItems = orderItems.size();
        final int[] processedCount = {0};

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        for (OrderItem item : orderItems) {
            apiService.getFoodByID(item.getFoodId()).enqueue(new Callback<FoodItem>() {
                @Override
                public void onResponse(Call<FoodItem> call, Response<FoodItem> responseFood) {
                    if (responseFood.isSuccessful() && responseFood.body() != null) {
                        itemList.add(new OrderItemWithFood(item, responseFood.body()));
                        Log.d("API", "Thêm món: " + responseFood.body().getName() + ", SL: " + item.getQuantity());
                    } else {
                        Log.e("API", "Không tìm thấy thông tin món ăn cho ID: " + item.getFoodId());
                    }
                    checkIfAllItemsProcessed(++processedCount[0], totalItems);
                }

                @Override
                public void onFailure(Call<FoodItem> call, Throwable t) {
                    Log.e("API", "Lỗi khi lấy món ăn: " + t.getMessage());
                    checkIfAllItemsProcessed(++processedCount[0], totalItems);
                }
            });
        }
    }

    // Kiểm tra xem đã xử lý xong tất cả món hay chưa, sau đó cập nhật UI
    private void checkIfAllItemsProcessed(int processedCount, int totalItems) {
        if (processedCount == totalItems) {
            updateUIFromIntent();
        }
    }

    // Xử lý lỗi khi gọi API đơn hàng
    private void handleOrderItemsError(int code) {
        Log.e("API", "Lỗi lấy đơn hàng. Code: " + code);
        showToast("Không lấy được dữ liệu đơn hàng");
    }

    // Xử lý thất bại kết nối API đơn hàng
    private void handleOrderItemsFailure(Throwable t) {
        Log.e("API", "Lỗi kết nối: " + t.getMessage());
        showToast("Lỗi kết nối");
    }

    // Cập nhật UI dựa trên dữ liệu intent và danh sách món
    private void updateUIFromIntent() {
        double amount = getIntent().getDoubleExtra("final_amount", 0.0);
        String formattedAmount = String.format("%,.0f", amount);

        txtOrderId.setText("Mã đơn: " + orderId);

        String status = getIntent().getStringExtra("status");
        String createdAt = getIntent().getStringExtra("created_at");
        String address = getIntent().getStringExtra("address");
        int discountPercent = getIntent().getIntExtra("discount_percent",0);

        if (status == null) status = "Đang xử lý";
        if (createdAt == null) createdAt = "Không rõ ngày";
        if (address == null) address = "Không rõ địa chỉ";
        txtDate.setText("Ngày tạo: " + createdAt);
        txtTotal.setText(formattedAmount + "đ");
        txtAddress.setText("Địa chỉ: " + address);
        txtDiscount.setText("Giảm giá: " + discountPercent +"%");
        txtStatus.setText("Trạng thái: " + status);
        setStatusColor(txtStatus, status);


        if (adapter == null) {
            adapter = new OrderItemAdapter(this, itemList);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        listView.setAdapter(adapter);

        if (status.equals("Đã giao")) {
            btnAddReview.setVisibility(View.VISIBLE);
            btnAddReview.setEnabled(true);
        }
        else
        {
            btnAddReview.setVisibility(View.GONE);
            btnAddReview.setEnabled(false);
        }

    }

    // Hiển thị Toast đơn giản
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setStatusColor(TextView tvStatus, String status) {
        int color;
        switch (status) {
            case "Đang chờ":
                color = 0xFFFFC107; // Vàng
                break;
            case "Đã tiếp nhận":
                color = 0xFFFF9800; // Cam
                break;
            case "Đang giao":
                color = 0xFF2196F3; // Xanh
                break;
            case "Đã giao":
                color = 0xFF4CAF50; // Xanh lá
                break;
            case "Hủy":
                color = 0xFFF44336; // Đỏ
                break;
            default:
                color = 0xFF000000; // Đen
        }
        tvStatus.setTextColor(color);
    }
}