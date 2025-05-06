package com.example.doan;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersFragment extends Fragment {

    private ListView orderListView;
    private List<Order> orderList;
    private OrderAdapter orderAdapter;
    public AdminOrdersFragment(){
        super(R.layout.acitivity_admin_orders);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        orderListView = view.findViewById(R.id.orderListView);
        orderList = getSampleOrders();

        // Khởi tạo OrderAdapter và gắn vào ListView
        orderAdapter = new OrderAdapter(getContext(), orderList);
        orderListView.setAdapter(orderAdapter);
    }

    private List<Order> getSampleOrders() {
        List<Order> list = new ArrayList<>();
        list.add(new Order("DH001", "Nguyễn Văn A", "21/04/2025 14:20", 500000, "Đang chuẩn bị"));
        list.add(new Order("DH002", "Trần Thị B", "21/04/2025 15:10", 750000, "Đã giao"));
        list.add(new Order("DH003", "Lê Văn C", "22/04/2025 10:00", 320000, "Hủy"));
        return list;
    }
}