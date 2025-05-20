package com.example.doan.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.CartManager;
import com.example.doan.Item;
import com.example.doan.databinding.CartItemBinding;
import com.example.doan.detailsActivity;

import java.util.Arrays;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private static List<Item> cartList;
    private final Context requireContext;
    static CartManager cartManager = CartManager.getInstance();
    static int quantity;

    public CartAdapter(List<Item> cartList, Context requireContext) {
        this.cartList = cartList;
        this.requireContext = requireContext;
    }

    public void setFilteredList(List<Item> filteredList) {
        cartList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding, requireContext);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;
        private final Context requireContext;

        public CartViewHolder(CartItemBinding binding, Context requireContext) {
            super(binding.getRoot());
            this.binding = binding;
            this.requireContext = requireContext;
        }

        public void bind(int position) {

            int price = Integer.parseInt(cartList.get(position).getItemPrice().substring(0, cartList.get(position).getItemPrice().length() - 1));
            quantity = Integer.parseInt(cartList.get(position).getItemQuantity());
            String total = (price * quantity) + "$";
            binding.tvName.setText(cartList.get(position).getItemName());
            binding.tvPrice.setText(total);
            binding.imageView.setImageResource(cartList.get(position).getItemImage());
            binding.quantity.setText(cartList.get(position).getItemQuantity());

            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(requireContext, detailsActivity.class);
                        intent.putExtra("MenuItemName", cartList.get(position).getItemName());
                        intent.putExtra("MenuItemPrice", cartList.get(position).getItemPrice());
                        intent.putExtra("MenuItemImage", cartList.get(position).getItemImage());
                        intent.putExtra("MenuItemQuantity", cartList.get(position).getItemQuantity());
                        requireContext.startActivity(intent);
                    }

                }
            });

            binding.btnMinus.setOnClickListener(v -> {
                if(quantity > 1) {
                    quantity--;
                    cartList.get(position).setItemQuantity(String.valueOf(quantity));
                    binding.quantity.setText(String.valueOf(quantity));
                    String Total = quantity * price + "$";
                    cartManager.setTotalOrder(cartManager.getTotalOrder() - price);
                    binding.tvPrice.setText(Total);
                }
            });

            binding.btnPlus.setOnClickListener(v -> {
                quantity++;
                cartList.get(position).setItemQuantity(String.valueOf(quantity));
                binding.quantity.setText(String.valueOf(quantity));
                String Total = quantity * price + "$";
                binding.tvPrice.setText(Total);
            });
        }



    }


}
