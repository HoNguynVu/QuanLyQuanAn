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
import com.example.doan.UserItem;
import com.example.doan.databinding.UserHomePopularItemBinding;
import com.example.doan.UserDetailsActivity;

import java.util.List;

public class UserHomePopularItemAdapter extends RecyclerView.Adapter<UserHomePopularItemAdapter.myViewHolder> {
    private final Context requireContext;
    private final List<UserItem> itemList;

    public UserHomePopularItemAdapter(Context requireContext, List<UserItem> itemList) {
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

        public void bind(UserItem item) {
            binding.tvName.setText(item.getItemName());
            binding.tvPrice.setText(String.valueOf(item.getItemPrice()));
            binding.tvDetail.setText(item.getDetail());
            String imageUrl = item.getImageUrl();

            Glide.with(binding.getRoot().getContext()).load(imageUrl).into(binding.imageView);

            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Log.d(TAG, "Context class: " + requireContext.getClass().getName());
                        Intent intent = new Intent(requireContext, UserDetailsActivity.class);
                        intent.putExtra("MenuItemName", item.getItemName());
                        intent.putExtra("MenuItemPrice", item.getItemPrice());
                        intent.putExtra("MenuItemImageUrl", item.getImageUrl());
                        intent.putExtra("MenuItemQuantity", item.getItemQuantity());
                        intent.putExtra("MenuItemDescription", item.getDetail());
                        requireContext.startActivity(intent);
                    }

                }
            });
        }

    }
}