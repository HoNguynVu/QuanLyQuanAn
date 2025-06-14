package com.example.doan.ProfileUser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.Adapter.OrdersUserAdapter;
import com.example.doan.DatabaseClass.Order;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {

    private ListView ordersListView;
    private List<Order> orders;
    private OrdersUserAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_orders);

        initUI();
        setupBackButton();

        String userEmail = getUserEmail();
        fetchOrders(userEmail);
    }

    //Ánh xạ giao diện và thiết lập adapter cho ListView
    private void initUI() {
        ordersListView = findViewById(R.id.listOrders);
        orders = new ArrayList<>();
        ordersAdapter = new OrdersUserAdapter(this, orders);
        ordersListView.setAdapter(ordersAdapter);
    }

    //Lấy email người dùng từ SharedPreferences
    private String getUserEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }

    //Bắt sự kiện nút quay lại
    private void setupBackButton() {
        ImageView backButton = findViewById(R.id.imgBackMyOrder);
        backButton.setOnClickListener(view -> finish());
    }

    //Gọi API để lấy danh sách đơn hàng của người dùng
    private void fetchOrders(String email) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Order>> call = apiService.getOrdersByUser(email);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orders.clear();
                    orders.addAll(response.body());
                    ordersAdapter.notifyDataSetChanged();
                } else {
                    showToast("Không có dữ liệu đơn hàng");
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                showToast("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    //Hiển thị thông báo ngắn
    private void showToast(String message) {
        Toast.makeText(MyOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
