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
import com.example.doan.DatabaseClass.OrderItem;
import com.example.doan.UserActivity.UserDetailsActivity;
import com.example.doan.databinding.UserHistoryItemBinding;
import com.example.doan.databinding.UserHomePopularItemBinding;

import java.util.List;

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.myViewHolder> {

    private final Context requireContext;
    private final List<OrderItem> itemList;

    public UserHistoryAdapter(Context requireContext, List<OrderItem> itemList) {
        this.itemList = itemList;
        this.requireContext = requireContext;
    }

    public void updateData(List<OrderItem> newItems) {
        this.itemList.clear();
        this.itemList.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = UserHistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserHistoryAdapter.myViewHolder(binding, requireContext);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHistoryAdapter.myViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        private final UserHistoryItemBinding binding;
        private final Context requireContext;

        public myViewHolder(UserHistoryItemBinding binding, Context requireContext) {
            super(binding.getRoot());
            this.binding = binding;
            this.requireContext = requireContext;
        }

        public void bind(OrderItem item) {
            binding.itemName.setText(item.getName());
            binding.itemPrice.setText(String.valueOf(item.getPrice()));
            binding.itemDetail.setText(item.getDescription());
            String imageUrl = item.getImageUrl();

            Glide.with(binding.getRoot().getContext()).load(imageUrl).into(binding.image);

/*            binding.getRoot().setOnClickListener(new View.OnClickListener() {

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
            });*/
        }
    }
}
