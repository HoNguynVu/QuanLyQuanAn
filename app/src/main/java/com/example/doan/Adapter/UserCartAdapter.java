package com.example.doan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.User.CartLocalDb;
import com.example.doan.User.CartMeta;
import com.example.doan.User.UserCartManager;
import com.example.doan.UserFragment.UserNoteChangeFragment;
import com.example.doan.databinding.UserCartItemBinding;

import java.text.DecimalFormat;
import java.util.List;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.CartViewHolder> {
    private final List<FoodItem> cartList;
    private final Context requireContext;
    private final FragmentManager fragmentManager;
    public UserCartAdapter(Context requireContext, FragmentManager fragmentManager) {
        this.cartList = UserCartManager.getInstance().getCartItems();
        this.requireContext = requireContext;
        this.fragmentManager = fragmentManager;
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
            String total = formatCurrency(price * quantity[0]) + "đ";

            binding.tvName.setText(cartList.get(position).getName());
            binding.tvPrice.setText(total);
            String imageUrl = cartList.get(position).getImage_url();
            Glide.with(binding.getRoot().getContext()).load(imageUrl).into(binding.imageView);
            binding.quantity.setText(cartList.get(position).getItemQuantity());
            binding.note.setText(cartList.get(position).getNote());

            setNoteChangeView(position);
            setBtnMinus(quantity, position, price);
            setBtnPlus(quantity, position, price);
            setBtnDelete(quantity, position, price);
        }
        public void setNoteChangeView(int position) {
            binding.getRoot().setOnClickListener(v -> {
                FoodItem item = cartList.get(position);
                UserNoteChangeFragment sheet = new UserNoteChangeFragment();
                Bundle args = new Bundle();
                args.putString("note", binding.note.getText().toString());
                sheet.setArguments(args);

                // Nhận note mới từ BottomSheet
                sheet.setOnTextEnteredListener(newNote -> {
                    item.setNote(newNote);                          // cập nhật vào list
                    binding.note.setText(newNote);                  // cập nhật UI
                    notifyItemChanged(position);                    // cập nhật lại view

                    // Lưu vào Room
                    new Thread(() -> {
                        try {
                            CartLocalDb db = CartLocalDb.getInstance(requireContext);
                            db.cartItemDao().update(item);
                            Log.d("NoteUpdate", "✅ Note updated for cartId=" + item.getLocalId());
                        } catch (Exception e) {
                            Log.e("NoteUpdate", "❌ Lỗi khi update note: " + e.getMessage());
                        }
                    }).start();
                });

                sheet.show(fragmentManager, "UserNoteChangeFragment");
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
                    binding.tvPrice.setText(formatCurrency(newTotal) + "đ");

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
                binding.tvPrice.setText(formatCurrency(newTotal) + "đ");

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
    private static String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }


}
