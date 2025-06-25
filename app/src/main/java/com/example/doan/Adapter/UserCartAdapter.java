package com.example.doan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.User.CartLocalDb;
import com.example.doan.User.CartMeta;
import com.example.doan.User.UserCartManager;
import com.example.doan.databinding.UserCartItemBinding;
import com.example.doan.UserActivity.UserDetailsActivity;

import java.util.List;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.CartViewHolder> {
    private final List<FoodItem> cartList;
    private final Context requireContext;

    public UserCartAdapter(Context requireContext) {
        this.cartList = UserCartManager.getInstance().getCartItems();
        this.requireContext = requireContext;
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
            double price = cartList.get(position).getPrice();
            final int[] quantity = {Integer.parseInt(cartList.get(position).getItemQuantity())};
            String total = (price * quantity[0]) + "";

            binding.tvName.setText(cartList.get(position).getName());
            binding.tvPrice.setText(total);
            String imageUrl = cartList.get(position).getImage_url();
            Glide.with(binding.getRoot().getContext()).load(imageUrl).into(binding.imageView);
            binding.quantity.setText(cartList.get(position).getItemQuantity());
            binding.note.setText(cartList.get(position).getNote());

            setDetailView();
            setBtnMinus(quantity, position, price);
            setBtnPlus(quantity, position, price);
            setBtnDelete(quantity, position, price);
        }
        public void setDetailView() {
            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(requireContext, UserDetailsActivity.class);
                        intent.putExtra("MenuItemName", cartList.get(position).getName());
                        intent.putExtra("MenuItemPrice", cartList.get(position).getPrice());
                        intent.putExtra("MenuItemImageUrl", cartList.get(position).getImage_url());
                        intent.putExtra("MenuItemQuantity", cartList.get(position).getItemQuantity());
                        requireContext.startActivity(intent);
                    }

                }
            });
        }
        public void setBtnMinus(int[] quantity, int position, double price) {
            binding.btnMinus.setOnClickListener(v -> {
                if(quantity[0] > 1) {
                    quantity[0]--;
                    FoodItem item = cartList.get(position);
                    item.setItemQuantity(String.valueOf(quantity[0]));
                    binding.quantity.setText(String.valueOf(quantity[0]));

                    userCartManager.setTotalOrder(userCartManager.getTotalOrder() - price);
                    userCartManager.notifyTotalChanged();

                    double newTotal = quantity[0] * price;
                    binding.tvPrice.setText(String.valueOf(newTotal));

                    new Thread(() -> {
                        try {
                            CartLocalDb db = CartLocalDb.getInstance(requireContext);
                            db.cartItemDao().update(item); // Room dùng cartId để update
                            double totalOrder = userCartManager.getTotalOrder();
                            db.cartMetaDao().insert(new com.example.doan.User.CartMeta(totalOrder));
                        } catch (Exception e) {
                            Log.e("RoomUpdate", "Error updating quantity: " + e.getMessage());
                        }
                    }).start();
                }
            });
        }

        public void setBtnPlus(int[] quantity, int position, double price) {
            binding.btnPlus.setOnClickListener(v -> {
                quantity[0]++;
                FoodItem item = cartList.get(position);
                item.setItemQuantity(String.valueOf(quantity[0]));
                binding.quantity.setText(String.valueOf(quantity[0]));

                userCartManager.setTotalOrder(userCartManager.getTotalOrder() + price);
                userCartManager.notifyTotalChanged();

                double newTotal = quantity[0] * price;
                binding.tvPrice.setText(String.valueOf(newTotal));

                new Thread(() -> {
                    try {
                        CartLocalDb db = CartLocalDb.getInstance(requireContext);
                        db.cartItemDao().update(item); // Room dùng cartId để update
                        double totalOrder = userCartManager.getTotalOrder();
                        db.cartMetaDao().insert(new com.example.doan.User.CartMeta(totalOrder));

                    } catch (Exception e) {
                        Log.e("RoomUpdate", "Error updating quantity: " + e.getMessage());
                    }
                }).start();
            });
        }

        public void setBtnDelete(int[] quantity, int position, double price) {
            binding.btnDelete.setOnClickListener(v -> {
                if (position == RecyclerView.NO_POSITION || position >= cartList.size()) return;

                FoodItem item = cartList.get(position);
                int localId = item.getLocalId();

                // Tính tổng tiền mới
                double amountToRemove = price * quantity[0];
                double newTotal = Math.max(0, userCartManager.getTotalOrder() - amountToRemove);

                // 1. Xóa khỏi Room trước
                new Thread(() -> {
                    try {
                        CartLocalDb db = CartLocalDb.getInstance(requireContext);
                        db.cartItemDao().deleteByLocalId(localId);
                        db.cartMetaDao().insert(new CartMeta(newTotal));
                        Log.d("CartDelete", "🗑️ Đã xóa item cartId=" + localId + " khỏi Room");

                        // 2. Xử lý UI và UserCartManager trên main thread
                        ((Activity) requireContext).runOnUiThread(() -> {
                            cartList.remove(position);
                            notifyItemRemoved(position);

                            userCartManager.getCartItems().remove(item);
                            userCartManager.setTotalOrder(newTotal);
                            userCartManager.notifyTotalChanged();
                        });
                        Log.d("CartCount", "Cart item count: " + db.cartItemDao().getCartItemCount());
                    } catch (Exception e) {
                        Log.e("CartDelete", "❌ Lỗi khi xóa: " + e.getMessage());
                    }
                }).start();
            });
        }



    }


}
