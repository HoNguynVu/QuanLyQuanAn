package com.example.doan.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.databinding.CartItemBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<String> cartItems;
    private final List<String> cartItemPrice;
    private final List<Integer> cartImage;

    public CartAdapter(List<String> cartItems, List<String> cartItemPrice, List<Integer> cartImage) {
        this.cartItems = cartItems;
        this.cartItemPrice = cartItemPrice;
        this.cartImage = cartImage;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

public class CartViewHolder extends RecyclerView.ViewHolder{
    private final CartItemBinding binding;
    public CartViewHolder(CartItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(int position) {
        binding.tvName.setText(cartItems.get(position));
        binding.tvPrice.setText(cartItemPrice.get(position));
        binding.imageView.setImageResource(cartImage.get(position));
    }
}
}
