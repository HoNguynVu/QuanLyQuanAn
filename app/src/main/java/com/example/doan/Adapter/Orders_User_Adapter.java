package com.example.doan.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doan.DatabaseClass.Order;
import com.example.doan.R;

import java.text.DecimalFormat;
import java.util.List;

public class Orders_User_Adapter extends ArrayAdapter<Order> {
    private Context context;
    private List<Order> orders;

    public Orders_User_Adapter(@NonNull Context context, @NonNull List<Order> orders) {
        super(context, 0, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Order order = orders.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_orders_user, parent, false);
        }

        TextView txtOrderId = convertView.findViewById(R.id.txtOrderId_user);
        TextView txtAmount = convertView.findViewById(R.id.txtAmount);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        TextView txtStatus = convertView.findViewById(R.id.txtStatus);


        if (order != null) {
            txtOrderId.setText("Mã đơn: " + order.getOrderId());

            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedAmount = formatter.format(order.getFinalAmount());
            txtAmount.setText("Tổng tiền: " + formattedAmount + " đ");

            txtDate.setText("Ngày tạo: " + order.getCreatedAt());
            txtStatus.setText("Trạng thái: " + order.getStatus());
        }


        return convertView;
    }
}
