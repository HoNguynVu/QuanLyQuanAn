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
import com.example.doan.databinding.UserMenuItemBinding;
import com.example.doan.UserDetailsActivity;

import java.util.List;

public class UserMenuAdapter extends RecyclerView.Adapter<UserMenuAdapter.MenuViewHolder> {
    private static List<UserItem> cartList;
    private final Context requireContext;
    static UserCartManager userCartManager = UserCartManager.getInstance();

    public UserMenuAdapter(List<UserItem> cartList, Context requireContext) {
        this.cartList = cartList;
        this.requireContext = requireContext;
    }

    public void setFilteredList(List<UserItem> filteredList) {
        cartList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var binding = UserMenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        private final UserMenuItemBinding binding;
        private final Context requireContext;

        UserCartManager userCartManager = UserCartManager.getInstance();

        public MenuViewHolder(UserMenuItemBinding binding, Context requireContext) {
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
                        Intent intent = new Intent(requireContext, UserDetailsActivity.class);
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
