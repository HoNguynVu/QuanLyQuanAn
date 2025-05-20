package com.example.doan.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.CartManager;
import com.example.doan.Item;
import com.example.doan.databinding.ActivityCheckOutBinding;
import com.example.doan.databinding.CheckOutItemBinding;
import com.example.doan.detailsActivity;

import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder> {
    private static List<Item> cartList;
    private final Context requireContext;
    static CartManager cartManager = CartManager.getInstance();

    public CheckOutAdapter(List<Item> cartList, Context requireContext) {
        this.cartList = cartList;
        this.requireContext = requireContext;
    }


    @NonNull
    @Override
    public CheckOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = CheckOutItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CheckOutViewHolder(binding, requireContext);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CheckOutViewHolder extends RecyclerView.ViewHolder {
        private final CheckOutItemBinding binding;
        private final Context requireContext;

        CartManager cartManager = CartManager.getInstance();

        public CheckOutViewHolder(CheckOutItemBinding binding, Context requireContext) {
            super(binding.getRoot());
            this.binding = binding;
            this.requireContext = requireContext;
        }

        public void bind(int position) {

            int price = Integer.parseInt(cartList.get(position).getItemPrice().substring(0, cartList.get(position).getItemPrice().length() - 1));
            final int[] quantity = {Integer.parseInt(cartList.get(position).getItemQuantity())};
            String total = (price * quantity[0]) + "$";

            String foodName = cartList.get(position).getItemName() + "(" + cartList.get(position).getItemQuantity() + "x)";
            binding.tvFoodName.setText(foodName);
            binding.tvPrice.setText(total);
        }



    }


}
