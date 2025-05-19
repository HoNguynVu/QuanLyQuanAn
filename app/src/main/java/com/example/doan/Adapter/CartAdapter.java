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

import com.example.doan.Item;
import com.example.doan.databinding.CartItemBinding;
import com.example.doan.detailsActivity;

import java.util.Arrays;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Item> cartList;
    private final Context requireContext;

    private final int[] itemQuantities;

    public CartAdapter(List<Item> cartList, Context requireContext) {
        this.cartList = cartList;

        itemQuantities = new int[cartList.size()];
        this.requireContext = requireContext;
        Arrays.fill(itemQuantities, 1);
    }

    public void setFilteredList(List<Item> filteredList) {
        this.cartList = filteredList;
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

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;
        private final Context requireContext;

        public CartViewHolder(CartItemBinding binding, Context requireContext) {
            super(binding.getRoot());
            this.binding = binding;
            this.requireContext = requireContext;
        }

        public void bind(int position) {
            binding.tvName.setText(cartList.get(position).getItemName());
            binding.tvPrice.setText(cartList.get(position).getItemPrice());
            binding.imageView.setImageResource(cartList.get(position).getItemImage());

            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Log.d(TAG, "Context class: " + requireContext.getClass().getName());
                        Intent intent = new Intent(requireContext, detailsActivity.class);
                        intent.putExtra("MenuItemName", cartList.get(position).getItemName());
                        intent.putExtra("MenuItemPrice", cartList.get(position).getItemPrice());
                        intent.putExtra("MenuItemImage", cartList.get(position).getItemImage());
                        requireContext.startActivity(intent);
                    }

                }
            });


        }



    }


}
