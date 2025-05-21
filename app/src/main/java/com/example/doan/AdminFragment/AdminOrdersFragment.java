package com.example.doan.AdminFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.DatabaseClass.Order;
import com.example.doan.Adapter.OrderAdapter;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersFragment extends Fragment {

    private ListView orderListView;
    private List<Order> orderList;
    private OrderAdapter orderAdapter;

    public AdminOrdersFragment() {
        super(R.layout.activity_admin_orders);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        orderListView = view.findViewById(R.id.orderListView);
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(), orderList);
        orderListView.setAdapter(orderAdapter);

        loadOrdersFromServer();
    }


    private void loadOrdersFromServer() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Order>> call = apiService.getOrders();

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (!isAdded() || getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    orderAdapter.notifyDataSetChanged();

                    // Hiển thị text nếu danh sách rỗng
                    if (orderList.isEmpty()) {
                        orderListView.setVisibility(View.GONE);
                    } else {
                        orderListView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
