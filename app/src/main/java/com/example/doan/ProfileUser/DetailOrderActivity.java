package com.example.doan.ProfileUser;

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
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderActivity extends AppCompatActivity {

    private TextView txtHeader, txtOrderId, txtDate, txtStatus, txtTotal;
    private ListView listView;
    private List<OrderItemWithFood> itemList = new ArrayList<>();
    private OrderItemAdapter adapter;
    private Button btnBack;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        orderId = getIntent().getStringExtra("order_id");

        txtHeader = findViewById(R.id.txt_header);
        txtOrderId = findViewById(R.id.txt_order_id);
        txtDate = findViewById(R.id.txt_order_date);
        txtStatus = findViewById(R.id.txt_order_status);
        txtTotal = findViewById(R.id.txt_total_amount);
        listView = findViewById(R.id.lv_order_items);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        if (orderId == null || orderId.isEmpty()) {
            Toast.makeText(this, "Không có mã đơn hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        loadOrderDetails(orderId);
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
                        Toast.makeText(DetailOrderActivity.this, "Đơn hàng không có món nào", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int total = orderItems.size();
                    int[] count = {0};
                    int[] totalPrice = {0};

                    for (OrderItem item : orderItems) {
                        apiService.getFoodByID(item.getFoodId()).enqueue(new Callback<FoodItem>() {
                            @Override
                            public void onResponse(Call<FoodItem> call, Response<FoodItem> responseFood) {
                                if (responseFood.isSuccessful() && responseFood.body() != null) {
                                    FoodItem food = responseFood.body();
                                    itemList.add(new OrderItemWithFood(item, food));
                                    totalPrice[0] += food.getPrice() * item.getQuantity();
                                    Log.d("API", "Thêm món: " + food.getName() + ", SL: " + item.getQuantity());
                                } else {
                                    Log.e("API", "Không tìm thấy thông tin món ăn cho ID: " + item.getFoodId());
                                }

                                count[0]++;
                                if (count[0] == total) {
                                    updateUI(totalPrice[0]);
                                }
                            }

                            @Override
                            public void onFailure(Call<FoodItem> call, Throwable t) {
                                Log.e("API", "Lỗi khi lấy món ăn: " + t.getMessage());
                                count[0]++;
                                if (count[0] == total) {
                                    updateUI(totalPrice[0]);
                                }
                            }
                        });
                    }

                } else {
                    Log.e("API", "Lỗi lấy đơn hàng. Code: " + response.code());
                    Toast.makeText(DetailOrderActivity.this, "Không lấy được dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(DetailOrderActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(int totalPrice) {
        String formattedPrice = String.format("%,d", totalPrice);

        txtOrderId.setText("Mã đơn: " + orderId);

        // Nhận status và date từ Intent nếu có
        String status = getIntent().getStringExtra("status");
        String createdAt = getIntent().getStringExtra("created_at");

        if (status == null) status = "Đang xử lý";
        if (createdAt == null) createdAt = "Không rõ ngày";

        txtStatus.setText("Trạng thái: " + status);
        txtDate.setText("Ngày tạo: " + createdAt);
        txtTotal.setText("Tổng tiền: " + formattedPrice + "đ");

        adapter = new OrderItemAdapter(this, itemList);
        listView.setAdapter(adapter);
    }


}
