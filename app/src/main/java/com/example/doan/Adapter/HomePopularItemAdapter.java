package com.example.doan.Adapter;

import static android.content.ContentValues.TAG;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Item;
import com.example.doan.databinding.HomePopularItemBinding;
import com.example.doan.detailsActivity;

import java.util.List;

public class HomePopularItemAdapter extends RecyclerView.Adapter<HomePopularItemAdapter.myViewHolder> {
    private HomePopularItemBinding binding;
    private final Context requireContext;
    static List<Item> itemList;

    public HomePopularItemAdapter(Context requireContext, List<Item> itemList) {
        this.itemList = itemList;
        this.requireContext = requireContext;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = HomePopularItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new myViewHolder(binding, requireContext);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        private final HomePopularItemBinding binding;
        private final Context requireContext;

        public myViewHolder(HomePopularItemBinding binding, Context requireContext) {
            super(binding.getRoot());
            this.binding = binding;
            this.requireContext = requireContext;
        }

        public void bind(int position) {
            binding.tvName.setText(itemList.get(position).getItemName());
            binding.tvPrice.setText(itemList.get(position).getItemPrice());
            binding.imageView.setImageResource(itemList.get(position).getItemImage());

            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Log.d(TAG, "Context class: " + requireContext.getClass().getName());
                        Intent intent = new Intent(requireContext, detailsActivity.class);
                        intent.putExtra("MenuItemName", itemList.get(position).getItemName());
                        intent.putExtra("MenuItemPrice", itemList.get(position).getItemPrice());
                        intent.putExtra("MenuItemImage", itemList.get(position).getItemImage());
                        intent.putExtra("MenuItemQuantity", itemList.get(position).getItemQuantity());
                        requireContext.startActivity(intent);
                    }

                }
            });
        }

    }
}