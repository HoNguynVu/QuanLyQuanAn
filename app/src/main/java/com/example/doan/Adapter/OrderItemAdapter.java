package com.example.doan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.ProfileUser.DetailFoodActivity;
import com.example.doan.ProfileUser.OrderItemWithFood;
import com.example.doan.R;

import java.text.DecimalFormat;
import java.util.List;

public class OrderItemAdapter extends ArrayAdapter<OrderItemWithFood> {
    private Context context;
    private List<OrderItemWithFood> items;
    private boolean isAdminMode = false; // Biến để phân biệt admin mode

    public OrderItemAdapter(Context context, List<OrderItemWithFood> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.isAdminMode = false; // Mặc định là user mode
    }

    // Constructor cho admin mode
    public OrderItemAdapter(Context context, List<OrderItemWithFood> items, boolean isAdminMode) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.isAdminMode = isAdminMode;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_order_food, parent, false);
        }

        OrderItemWithFood item = items.get(position);
        FoodItem food = item.getFood();

        ImageView imgFood = convertView.findViewById(R.id.img_food);
        TextView txtName = convertView.findViewById(R.id.txt_food_name);
        TextView txtQty = convertView.findViewById(R.id.txt_food_quantity);
        TextView txtPrice = convertView.findViewById(R.id.txt_food_price);
        TextView txtNote = convertView.findViewById(R.id.txt_food_note);

        txtName.setText(food.getName());
        txtQty.setText("Số lượng: " + item.getOrderItem().getQuantity());
        txtPrice.setText("Giá: " + formatCurrency(food.getPrice()) + " đ");
        txtNote.setText("Ghi chú: " + item.getOrderItem().getNote());

        // Load ảnh bằng Glide (thêm thư viện Glide vào project)
        Glide.with(context)
                .load(food.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)  // ảnh mặc định nếu chưa load được
                .into(imgFood);

        // Chỉ cho phép click nếu không phải admin mode
        if (!isAdminMode) {
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailFoodActivity.class);
                intent.putExtra("id",String.valueOf(food.getId()));
                context.startActivity(intent);
            });
        } else {
            // Nếu là admin mode, vô hiệu hóa click
            convertView.setClickable(false);
            convertView.setFocusable(false);
            convertView.setOnClickListener(null);
        }

        return convertView;
    }
    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }
}
