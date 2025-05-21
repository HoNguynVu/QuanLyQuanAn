package com.example.doan.ProfileUser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.OrderAdapter;
import com.example.doan.Adapter.Orders_User_Adapter;
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
    private ListView listOrders;
    private List<Order> orderList;
    private Orders_User_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_orders);

        listOrders = findViewById(R.id.listOrders);
        orderList = new ArrayList<>();
        adapter = new Orders_User_Adapter(this, orderList);
        listOrders.setAdapter(adapter);

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String email = sp.getString("email", "");

        ImageView imgBack = findViewById(R.id.imgBackMyOrder);
        imgBack.setOnClickListener(view -> finish());

        fetchOrdersFromServer(email);

    }
        private void fetchOrdersFromServer(String email) {
            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<List<Order>> call = apiService.getOrdersByUser(email);

            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        orderList.clear();
                        orderList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MyOrdersActivity.this, "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Toast.makeText(MyOrdersActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

}