package com.example.doan.AdminActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.example.doan.ProfileUser.OrderItemWithFood;
import com.example.doan.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderDetailActivity extends AppCompatActivity {

    private TextView txtHeader, txtOrderId, txtDate, txtStatus, txtTotal, txtCustomerName, txtCustomerPhone, txtAddress, txtDiscount;
    private ListView listView;
    private List<OrderItemWithFood> itemList = new ArrayList<>();
    private OrderItemAdapter adapter;
    private Button btnBack;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);
        orderId = getIntent().getStringExtra("order_id");
        if (orderId == null || orderId.isEmpty()) {
            Toast.makeText(this, "Không có mã đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initViews();
        loadOrderDetails(orderId);
    }
    private void initViews()
    {
        orderId = getIntent().getStringExtra("order_id");
        txtHeader = findViewById(R.id.txt_header);
        txtOrderId = findViewById(R.id.txt_order_id);
        txtDate = findViewById(R.id.txt_order_date);
        txtAddress = findViewById(R.id.txt_delivery_address);
        txtDiscount = findViewById(R.id.txt_discount_info);
        txtStatus = findViewById(R.id.txt_order_status);
        txtTotal = findViewById(R.id.txt_total_amount);
        txtCustomerName = findViewById(R.id.txt_customer_name);
        txtCustomerPhone = findViewById(R.id.txt_customer_phone);
        listView = findViewById(R.id.lv_order_items);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }
    private void loadOrderDetails(String orderId) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Log.d("API", "Gửi yêu cầu lấy đơn hàng với orderId = " + orderId);
        apiService.getOrderItems(orderId).enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderItem> orderItems = response.body();
                    Log.d("API", "Số món ăn trong đơn hàng: " + orderItems.size());

                    if (orderItems.isEmpty()) {
                        Toast.makeText(AdminOrderDetailActivity.this, "Đơn hàng không có món nào", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int total = orderItems.size();
                    int[] count = {0};

                    for (OrderItem item : orderItems) {
                        apiService.getFoodByID(item.getFoodId()).enqueue(new Callback<FoodItem>() {
                            @Override
                            public void onResponse(Call<FoodItem> call, Response<FoodItem> responseFood) {
                                if (responseFood.isSuccessful() && responseFood.body() != null) {
                                    FoodItem food = responseFood.body();
                                    itemList.add(new OrderItemWithFood(item, food));
                                    Log.d("API", "Thêm món: " + food.getName() + ", SL: " + item.getQuantity());
                                } else {
                                    Log.e("API", "Không tìm thấy thông tin món ăn cho ID: " + item.getFoodId());
                                }

                                count[0]++;
                                if (count[0] == total) {
                                    updateUIFromIntent();
                                }
                            }

                            @Override
                            public void onFailure(Call<FoodItem> call, Throwable t) {
                                Log.e("API", "Lỗi khi lấy món ăn: " + t.getMessage());
                                count[0]++;
                                if (count[0] == total) {
                                    updateUIFromIntent();
                                }
                            }
                        });
                    }

                } else {
                    Log.e("API", "Lỗi lấy đơn hàng. Code: " + response.code());
                    Toast.makeText(AdminOrderDetailActivity.this, "Không lấy được dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(AdminOrderDetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUIFromIntent() {
        double amount = getIntent().getDoubleExtra("final_amount", 0.0);
        String customerName = getIntent().getStringExtra("customer_name");
        String customerPhone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        String discountCode = getIntent().getStringExtra("discount_code");
        
        txtOrderId.setText("Mã đơn: " + orderId);
        txtCustomerName.setText("Khách hàng: " + (customerName != null ? customerName : "Không xác định"));
        
        // Kiểm tra xem TextView có null không
        if (txtCustomerPhone != null) {
            txtCustomerPhone.setText("Số điện thoại: " + (customerPhone != null ? customerPhone : "Chưa có số điện thoại"));
        } else {
            Log.e("AdminOrderDetail", "txtCustomerPhone is null!");
        }
        
        if (txtAddress != null) {
            txtAddress.setText("Địa chỉ giao hàng: " + (address != null ? address : "Chưa có địa chỉ"));
        } else {
            Log.e("AdminOrderDetail", "txtAddress is null!");
        }
        
        if (txtDiscount != null) {
            txtDiscount.setText("Mã giảm giá: " + (discountCode != null ? discountCode : "Không có mã giảm giá"));
        } else {
            Log.e("AdminOrderDetail", "txtDiscount is null!");
        }

        String status = getIntent().getStringExtra("status");
        String createdAt = getIntent().getStringExtra("created_at");

        if (status == null) status = "Đang xử lý";
        if (createdAt == null) createdAt = "Không rõ ngày";
        txtStatus.setText("Trạng thái: " + status);
        setStatusColor(txtStatus, status);
        txtDate.setText("Ngày tạo: " + createdAt);
        txtTotal.setText("Tổng tiền: " + formatCurrency(amount) + " VND");

        adapter = new OrderItemAdapter(this, itemList, true); // true = admin mode
        listView.setAdapter(adapter);
    }
    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
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
