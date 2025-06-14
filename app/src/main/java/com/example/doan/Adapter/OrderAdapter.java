package com.example.doan.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.doan.AdminActivity.AdminOrderDetailActivity;
import com.example.doan.DatabaseClass.Order;
import com.example.doan.DatabaseClassResponse.StatusResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends ArrayAdapter<Order> {

    private final Context context;
    private final List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        super(context, 0, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = orders.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }        TextView tvId = convertView.findViewById(R.id.tvOrderId);
        TextView tvTime = convertView.findViewById(R.id.tvOrderTime);
        TextView tvAmount = convertView.findViewById(R.id.tvTotalAmount);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        TextView tvCustomerName = convertView.findViewById(R.id.tvCustomerName);
        Button btnUpdate = convertView.findViewById(R.id.btnUpdateStatus);
        tvId.setText("Mã đơn: " + order.getOrderId());
        tvTime.setText("Thời gian: " + order.getCreatedAt());
        tvAmount.setText("Tổng tiền: " + formatCurrency(order.getFinalAmount()) + " VND");
        tvStatus.setText("Trạng thái: " + order.getStatus());
        tvCustomerName.setText("Khách hàng: " + (order.getCustomerName() != null ? order.getCustomerName() : "Không xác định"));
        setStatusColor(tvStatus, order.getStatus());btnUpdate.setOnClickListener(v -> {
            String[] statuses = {"Đang chờ", "Đang chuẩn bị", "Đã giao", "Hủy"};

            new AlertDialog.Builder(context)
                    .setTitle("Chọn trạng thái mới")
                    .setItems(statuses, (dialog, which) -> {
                        String selectedStatus = statuses[which];
                        order.setStatus(selectedStatus);
                        tvStatus.setText("Trạng thái: " + selectedStatus);
                        setStatusColor(tvStatus, selectedStatus);

                        Toast.makeText(context, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();

                        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                        Call<StatusResponse> call = apiService.updateOrderStatus(
                                Integer.parseInt(order.getOrderId()),
                                selectedStatus
                        );

                        call.enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                    Toast.makeText(context, "Đã cập nhật trạng thái lên server", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Lỗi cập nhật server: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                Toast.makeText(context, "Không thể kết nối đến server", Toast.LENGTH_SHORT).show();
                            }
                        });

                    })
                    .show();
        });        // Thêm click listener cho toàn bộ item để xem chi tiết
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminOrderDetailActivity.class);
            intent.putExtra("order_id", order.getOrderId());
            intent.putExtra("status", order.getStatus());
            intent.putExtra("created_at", order.getCreatedAt());
            intent.putExtra("final_amount", order.getFinalAmount());
            intent.putExtra("customer_name", order.getCustomerName());
            intent.putExtra("user_id", order.getUser_id());
            context.startActivity(intent);
        });

        return convertView;
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
            case "Đang chuẩn bị":
                color = 0xFFFF9800; // Cam
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
