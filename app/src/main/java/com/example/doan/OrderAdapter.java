package com.example.doan;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import androidx.annotation.NonNull;

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        super(context, 0, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Lấy đối tượng đơn hàng tại vị trí hiện tại
        Order order = orders.get(position);

        // Inflate layout item_order nếu convertView rỗng
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }

        // Khởi tạo các View từ item_order.xml
        TextView tvId = convertView.findViewById(R.id.tvOrderId);
        TextView tvCustomer = convertView.findViewById(R.id.tvCustomerName);
        TextView tvTime = convertView.findViewById(R.id.tvOrderTime);
        TextView tvAmount = convertView.findViewById(R.id.tvTotalAmount);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        Button btnUpdate = convertView.findViewById(R.id.btnUpdateStatus);

        // Gán dữ liệu vào các TextView
        tvId.setText("Mã đơn: " + order.id);
        tvCustomer.setText("Tên khách: " + order.customerName);
        tvTime.setText("Thời gian: " + order.orderTime);
        tvAmount.setText("Tổng tiền: " + order.totalAmount + "đ");
        tvStatus.setText("Trạng thái: " + order.status);

        // Cập nhật trạng thái đơn hàng khi nhấn nút
        btnUpdate.setOnClickListener(v -> {
            String[] statuses = {"Đang chuẩn bị", "Đã giao", "Hủy"};
            new AlertDialog.Builder(context)
                    .setTitle("Chọn trạng thái mới")
                    .setItems(statuses, (dialog, which) -> {
                        order.status = statuses[which];
                        tvStatus.setText("Trạng thái: " + order.status);
                        Toast.makeText(context, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });

        // Trả về view của item trong ListView
        return convertView;
    }
}

