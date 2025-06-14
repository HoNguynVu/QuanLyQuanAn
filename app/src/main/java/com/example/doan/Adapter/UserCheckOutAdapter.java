package com.example.doan.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.databinding.UserCheckOutItemBinding;

import java.util.List;

public class UserCheckOutAdapter extends RecyclerView.Adapter<UserCheckOutAdapter.CheckOutViewHolder> {
    private static List<FoodItem> cartList;
    private final Context requireContext;

    public UserCheckOutAdapter(List<FoodItem> cartList, Context requireContext) {
        this.cartList = cartList;
        this.requireContext = requireContext;
    }


    @NonNull
    @Override
    public CheckOutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = UserCheckOutItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        private final UserCheckOutItemBinding binding;
        private final Context requireContext;

        public CheckOutViewHolder(UserCheckOutItemBinding binding, Context requireContext) {
            super(binding.getRoot());
            this.binding = binding;
            this.requireContext = requireContext;
        }

        public void bind(int position) {
            double price = cartList.get(position).getPrice();
            final int[] quantity = {Integer.parseInt(cartList.get(position).getItemQuantity())};
            String total = (price * quantity[0]) + "";

            String foodName = cartList.get(position).getName() + " (" + cartList.get(position).getItemQuantity() + "x)";
            binding.tvFoodName.setText(foodName);
            binding.tvPrice.setText(total);
        }



    }


}
