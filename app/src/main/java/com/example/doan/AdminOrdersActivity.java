package com.example.doan;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersActivity extends AppCompatActivity {

    private ListView orderListView;
    private List<Order> orderList;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_admin_orders);

        orderListView = findViewById(R.id.orderListView);
        orderList = getSampleOrders();

        // Khởi tạo OrderAdapter và gắn vào ListView
        orderAdapter = new OrderAdapter(this, orderList);
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
