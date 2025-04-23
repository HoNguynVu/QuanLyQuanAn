package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> itemList = new ArrayList<>();

    public MenuFragment() {
        super(R.layout.fragment_menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true); // ✅ tăng hiệu năng hiển thị

        menuAdapter = new MenuAdapter(getContext(), itemList);
        recyclerView.setAdapter(menuAdapter);

        loadMenuFromFirebase();
        view.findViewById(R.id.btnAddMenu).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AdminAddMenuItem.class);
            startActivity(intent);
        });
    }

    private void loadMenuFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("menu");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                menuAdapter.notifyDataSetChanged();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    MenuItem item = snap.getValue(MenuItem.class);
                    if (item != null) {
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

        private final List<MenuItem> itemList;
        private final Context context;

        public MenuAdapter(Context context, List<MenuItem> itemList) {
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
            MenuItem item = itemList.get(position);
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
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        static class MenuViewHolder extends RecyclerView.ViewHolder {
            TextView txtName, txtCategory, txtPrice;
            ImageView imgMenu;

            public MenuViewHolder(@NonNull View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txtName);
                txtCategory = itemView.findViewById(R.id.txtCategory);
                txtPrice = itemView.findViewById(R.id.txtPrice);
                imgMenu = itemView.findViewById(R.id.imgMenu);
            }
        }
    }
}