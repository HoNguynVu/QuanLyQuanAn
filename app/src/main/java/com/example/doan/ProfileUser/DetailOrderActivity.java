package com.example.doan.ProfileUser;
import com.example.doan.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.Adapter.OrderItemAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.OrderItem;
import com.example.doan.ProfileUser.OrderItemWithFood;
import com.example.doan.Network.APIService;

import com.example.doan.Network.RetrofitClient;

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

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        // Nhận orderId từ intent
        orderId = getIntent().getStringExtra("order_id");

        // Ánh xạ view
        txtHeader = findViewById(R.id.txt_header);
        txtOrderId = findViewById(R.id.txt_order_id);
        txtDate = findViewById(R.id.txt_order_date);
        txtStatus = findViewById(R.id.txt_order_status);
        txtTotal = findViewById(R.id.txt_total_amount);
        listView = findViewById(R.id.lv_order_items);

        // Gọi dữ liệu
        loadOrderDetails(orderId);
    }

    private void loadOrderDetails(String orderId) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        // Lấy danh sách order items
        apiService.getOrderItems(orderId).enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderItem> orderItems = response.body();

                    // Load từng món từ bảng food
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
                                }
                                count[0]++;
                                if (count[0] == total) {
                                    updateUI(totalPrice[0]);
                                }
                            }

                            @Override
                            public void onFailure(Call<FoodItem> call, Throwable t) {
                                count[0]++;
                                if (count[0] == total) {
                                    updateUI(totalPrice[0]);
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(DetailOrderActivity.this, "Không lấy được dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                Toast.makeText(DetailOrderActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(int totalPrice) {
        txtOrderId.setText("Mã đơn: " + orderId);
        txtStatus.setText("Đang xử lý"); // bạn có thể lấy từ intent nếu cần
        txtDate.setText("Ngày tạo: 2025-05-18"); // tuỳ ý thay đổi
        txtTotal.setText("Tổng tiền: " + totalPrice + "đ");

        adapter = new OrderItemAdapter(this, itemList);
        listView.setAdapter(adapter);
    }
}
