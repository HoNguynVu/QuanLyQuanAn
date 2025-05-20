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
import com.example.doan.databinding.MenuItemBinding;
import com.example.doan.detailsActivity;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private static List<Item> cartList;
    private final Context requireContext;
    static CartManager cartManager = CartManager.getInstance();

    public MenuAdapter(List<Item> cartList, Context requireContext) {
        this.cartList = cartList;
        this.requireContext = requireContext;
    }

    public void setFilteredList(List<Item> filteredList) {
        cartList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = MenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuViewHolder(binding, requireContext);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        private final MenuItemBinding binding;
        private final Context requireContext;

        CartManager cartManager = CartManager.getInstance();

        public MenuViewHolder(MenuItemBinding binding, Context requireContext) {
            super(binding.getRoot());
            this.binding = binding;
            this.requireContext = requireContext;
        }

        public void bind(int position) {

            int price = Integer.parseInt(cartList.get(position).getItemPrice().substring(0, cartList.get(position).getItemPrice().length() - 1));
            final int[] quantity = {Integer.parseInt(cartList.get(position).getItemQuantity())};
            String total = (price * quantity[0]) + "$";
            binding.tvName.setText(cartList.get(position).getItemName());
            binding.tvPrice.setText(total);
            binding.imageView.setImageResource(cartList.get(position).getItemImage());

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

        }



    }


}
