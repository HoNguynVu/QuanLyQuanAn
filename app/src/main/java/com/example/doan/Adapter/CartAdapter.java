package com.example.doan.Adapter;

import android.content.Context;
import android.content.Intent;
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
    private OnClickListener itemClickListener;

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
        return new CartViewHolder(binding);
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

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if(itemClickListener != null)
                            itemClickListener.onItemClick(position);
                    }

                    Intent intent = new Intent(requireContext, detailsActivity.class);
                    intent.putExtra("MenuItemName", cartList.get(position).getItemName());
                    intent.putExtra("MenuItemPrice", cartList.get(position).getItemPrice());
                    intent.putExtra("MenuItemImage", cartList.get(position).getItemImage());
                    requireContext.startActivity(intent);
                }
            });
        }

        public void bind(int position) {
            binding.tvName.setText(cartList.get(position).getItemName());
            binding.tvPrice.setText(cartList.get(position).getItemPrice());
            binding.imageView.setImageResource(cartList.get(position).getItemImage());
            binding.cartItemQuantity.setText(String.valueOf(itemQuantities[position]));

            binding.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseQuantity(getAdapterPosition());
                }
            });

            binding.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseQuantity(getAdapterPosition());
                }
            });

            binding.trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION)
                        deleteItem(getAdapterPosition());
                }
            });
        }

        private void decreaseQuantity(int position) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--;
                binding.cartItemQuantity.setText(String.valueOf(itemQuantities[position]));
            }
        }

        private void increaseQuantity(int position) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++;
                binding.cartItemQuantity.setText(String.valueOf(itemQuantities[position]));
            }
        }


        private void deleteItem(int position) {
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
        }
    }

    public interface OnClickListener {
        void onItemClick(int position);
    }


}
