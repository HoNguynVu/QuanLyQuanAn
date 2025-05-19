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

import java.util.List;

public class OrderItemAdapter extends ArrayAdapter<OrderItemWithFood> {
    private Context context;
    private List<OrderItemWithFood> items;

    public OrderItemAdapter(Context context, List<OrderItemWithFood> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
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

        txtName.setText(food.getName());
        txtQty.setText("Số lượng: " + item.getOrderItem().getQuantity());
        txtPrice.setText("Giá: " + food.getPrice() + "đ");

        // Load ảnh bằng Glide (thêm thư viện Glide vào project)
        Glide.with(context)
                .load(food.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)  // ảnh mặc định nếu chưa load được
                .into(imgFood);
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailFoodActivity.class);
            intent.putExtra("id", food.getId());
            intent.putExtra("name", food.getName());
            intent.putExtra("price", food.getPrice());
            intent.putExtra("imageUrl", food.getImageUrl());
            intent.putExtra("description", food.getDescription());
            context.startActivity(intent);
        });
        return convertView;
    }
}
