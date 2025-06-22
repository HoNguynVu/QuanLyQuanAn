package com.example.doan.Adapter;

import static android.content.ContentValues.TAG;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.databinding.UserHomePopularItemBinding;
import com.example.doan.UserActivity.UserDetailsActivity;

import java.text.DecimalFormat;
import java.util.List;

public class UserHomePopularItemAdapter extends RecyclerView.Adapter<UserHomePopularItemAdapter.myViewHolder> {
    private final Context requireContext;
    private final List<FoodItem> itemList;

    public UserHomePopularItemAdapter(Context requireContext, List<FoodItem> itemList) {
        this.itemList = itemList;
        this.requireContext = requireContext;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = UserHomePopularItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new myViewHolder(binding, requireContext);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        private final UserHomePopularItemBinding binding;
        private final Context requireContext;

        public myViewHolder(UserHomePopularItemBinding binding, Context requireContext) {
            super(binding.getRoot());
            this.binding = binding;
            this.requireContext = requireContext;
        }

        public void bind(FoodItem item) {
            binding.tvName.setText(item.getName());
            binding.tvPrice.setText(formatCurrency(item.getPrice())+"đ");
            binding.tvDetail.setText(item.getDescription());
            String imageUrl = item.getImageUrl();
            Glide.with(binding.getRoot().getContext()).load(imageUrl).into(binding.imageView);

            setDetailView(item);
        }

        public void setDetailView(FoodItem item) {
            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Log.d(TAG, "Context class: " + requireContext.getClass().getName());
                        Intent intent = new Intent(requireContext, UserDetailsActivity.class);
                        intent.putExtra("MenuItemID", item.getId());
                        intent.putExtra("MenuItemName", item.getName());
                        intent.putExtra("MenuItemPrice", item.getPrice());
                        intent.putExtra("MenuItemImageUrl", item.getImageUrl());
                        intent.putExtra("MenuItemQuantity", item.getItemQuantity());
                        intent.putExtra("MenuItemDescription", item.getDescription());
                        requireContext.startActivity(intent);
                    }

                }
            });
        }
    }
    private static String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }
}