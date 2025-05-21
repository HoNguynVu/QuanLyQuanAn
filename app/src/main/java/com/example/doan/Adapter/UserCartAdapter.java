package com.example.doan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.UserCartManager;
import com.example.doan.UserItem;
import com.example.doan.databinding.UserCartItemBinding;
import com.example.doan.UserDetailsActivity;

import java.util.List;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.CartViewHolder> {
    private static List<UserItem> cartList;
    private final Context requireContext;
    static UserCartManager userCartManager = UserCartManager.getInstance();

    public UserCartAdapter(List<UserItem> cartList, Context requireContext) {
        this.cartList = cartList;
        this.requireContext = requireContext;
    }

    public void setFilteredList(List<UserItem> filteredList) {
        cartList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = UserCartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        private final UserCartItemBinding binding;
        private final Context requireContext;

        UserCartManager userCartManager = UserCartManager.getInstance();

        public CartViewHolder(UserCartItemBinding binding, Context requireContext) {
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
            binding.quantity.setText(cartList.get(position).getItemQuantity());

            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(requireContext, UserDetailsActivity.class);
                        intent.putExtra("MenuItemName", cartList.get(position).getItemName());
                        intent.putExtra("MenuItemPrice", cartList.get(position).getItemPrice());
                        intent.putExtra("MenuItemImage", cartList.get(position).getItemImage());
                        intent.putExtra("MenuItemQuantity", cartList.get(position).getItemQuantity());
                        requireContext.startActivity(intent);
                    }

                }
            });

            binding.btnMinus.setOnClickListener(v -> {
                if(quantity[0] > 1) {
                    quantity[0]--;
                    cartList.get(position).setItemQuantity(String.valueOf(quantity[0]));
                    binding.quantity.setText(String.valueOf(quantity[0]));
                    String Total = quantity[0] * price + "$";
                    binding.tvPrice.setText(Total);

                    userCartManager.setTotalOrder(userCartManager.getTotalOrder() - price);
                    userCartManager.notifyTotalChanged();
                }
            });

            binding.btnPlus.setOnClickListener(v -> {
                quantity[0]++;
                cartList.get(position).setItemQuantity(String.valueOf(quantity[0]));
                binding.quantity.setText(String.valueOf(quantity[0]));
                String Total = quantity[0] * price + "$";
                binding.tvPrice.setText(Total);

                userCartManager.setTotalOrder(userCartManager.getTotalOrder() + price);
                userCartManager.notifyTotalChanged();
            });

            binding.btnDelete.setOnClickListener(v -> {
               cartList.remove(position);
               notifyItemRemoved(position);
               userCartManager.setTotalOrder(userCartManager.getTotalOrder() - price * quantity[0]);
               userCartManager.notifyTotalChanged();
            });
        }



    }


}
