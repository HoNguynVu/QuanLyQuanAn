package com.example.doan.AdminActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClassResponse.FoodListResponse;
import com.example.doan.DatabaseClassResponse.GenericResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            selectedCategory = getArguments().getString("category", "");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        loadMenuFromServer();  // Hàm bạn viết để reload danh sách
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        menuAdapter = new MenuAdapter(getContext(), itemList);
        recyclerView.setAdapter(menuAdapter);

        loadMenuFromServer();
    }

    private void loadMenuFromServer() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<FoodListResponse> call = apiService.getFoodsByCategory(selectedCategory);

        call.enqueue(new Callback<FoodListResponse>() {
            @Override
            public void onResponse(Call<FoodListResponse> call, Response<FoodListResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().status)) {
                    itemList.clear();
                    itemList.addAll(response.body().data);
                    menuAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            holder.txtAmount.setText("Số lượng: " + item.getAvailable() + "");


            Glide.with(context)
                    .load(item.getImageUrl())
                    .override(100, 100)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.loading_spinner)
                    .error(R.drawable.error_image)
                    .into(holder.imgMenu);
            Log.d("ImageURL", "URL: " + item.getImageUrl());


            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, AdminEditFoodItem.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("category", item.getCategory());
                intent.putExtra("price", item.getPrice());
                intent.putExtra("imageUrl", item.getImageUrl());
                intent.putExtra("id", item.getId());
                intent.putExtra("amount", item.getAvailable());
                intent.putExtra("description", item.getDescription());
                context.startActivity(intent);
            });

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xoá")
                        .setMessage("Bạn có chắc chắn muốn xoá \"" + item.getName() + "\"?")
                        .setPositiveButton("Xoá", (dialog, which) -> {
                            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                            Call<GenericResponse> call = apiService.deleteFood(item.getId());

                            call.enqueue(new Callback<GenericResponse>() {
                                @Override
                                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                                    if (response.isSuccessful() && response.body() != null && "success".equals(response.body().status)) {
                                        itemList.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(holder.getAdapterPosition());
                                        Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Xoá thất bại: " + response.body().message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<GenericResponse> call, Throwable t) {
                                    Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
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
            TextView txtAmount;
            public MenuViewHolder(@NonNull View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txtName);
                txtCategory = itemView.findViewById(R.id.txtCategory);
                txtPrice = itemView.findViewById(R.id.txtPrice);
                txtAmount = itemView.findViewById(R.id.txtAmount);
                imgMenu = itemView.findViewById(R.id.imgMenu);
                btnDelete = itemView.findViewById(R.id.btnDelete);
                btnEdit = itemView.findViewById(R.id.btnEdit);
            }
        }
    }
}
