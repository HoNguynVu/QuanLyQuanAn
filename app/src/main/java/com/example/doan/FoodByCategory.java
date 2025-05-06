package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FoodByCategory extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<FoodItem> itemList = new ArrayList<>();
    private String selectedCategory;

    public FoodByCategory() {
        super(R.layout.menu_by_category);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Cho phép fragment tạo menu riêng

        if (getArguments() != null) {
            selectedCategory = getArguments().getString("category", ""); // lấy loại món từ Bundle
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true); // ✅ tăng hiệu năng hiển thị

        menuAdapter = new MenuAdapter(getContext(), itemList);
        recyclerView.setAdapter(menuAdapter);

        loadMenuFromFirebase();

    }

    private void loadMenuFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("menu");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                menuAdapter.notifyDataSetChanged();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    FoodItem item = snap.getValue(FoodItem.class);
                    if (item != null && item.getCategory().equalsIgnoreCase(selectedCategory)) {
                        item.setKey(snap.getKey()); // Lưu lại key Firebase của món
                        itemList.add(item);
                        menuAdapter.notifyItemInserted(itemList.size() - 1);

                        // ✅ preload ảnh nếu cần
                        Glide.with(requireContext())
                                .load(item.getImageUrl())
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .preload();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải menu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
        private final List<FoodItem> itemList;
        private final Context context;

        public MenuAdapter(Context context, List<FoodItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);

            return new MenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
            FoodItem item = itemList.get(position);
            holder.txtName.setText(item.getName());
            holder.txtCategory.setText(item.getCategory());
            holder.txtPrice.setText(item.getPrice() + "đ");

            Glide.with(context)
                    .load(item.getImageUrl())
                    .override(100, 100) // kích thước cố định
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.loading_spinner)
                    .error(R.drawable.error_image)
                    .into(holder.imgMenu);

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xoá")
                        .setMessage("Bạn có chắc chắn muốn xoá món \"" + item.getName() + "\" không?")
                        .setPositiveButton("Xoá", (dialog, which) -> {
                            if (item.getKey() != null) {
                                FirebaseDatabase.getInstance()
                                        .getReference("menu")
                                        .child(item.getKey())
                                        .removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            int position1 = holder.getAdapterPosition();
                                            if (position1 != RecyclerView.NO_POSITION) {

                                                Toast.makeText(context, "Đã xoá món", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Lỗi khi xoá: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            });

            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, AdminEditFoodItem.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("category", item.getCategory());
                intent.putExtra("price", item.getPrice());
                intent.putExtra("imageUrl", item.getImageUrl());
                intent.putExtra("key", item.getKey());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        static class MenuViewHolder extends RecyclerView.ViewHolder {
            TextView txtName, txtCategory, txtPrice;
            ImageView imgMenu;
            AppCompatButton btnEdit, btnDelete;
            public MenuViewHolder(@NonNull View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txtName);
                txtCategory = itemView.findViewById(R.id.txtCategory);
                txtPrice = itemView.findViewById(R.id.txtPrice);
                imgMenu = itemView.findViewById(R.id.imgMenu);
                btnDelete = itemView.findViewById(R.id.btnDelete);
                btnEdit = itemView.findViewById(R.id.btnEdit);
            }
        }
    }
}