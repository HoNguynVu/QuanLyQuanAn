package com.example.doan.UserActivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.Adapter.ReviewAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.Review;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.User.CartLocalDb;
import com.example.doan.User.UserCartManager;
import com.example.doan.databinding.UserActivityDetailsBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends AppCompatActivity {
    UserActivityDetailsBinding binding;
    int quantity;
    int foodID;
    String foodName;
    double foodPrice;
    String foodQuantity;
    String foodImageUrl;
    String foodDescription;
    private TextView txtRatingAvg;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList = new ArrayList<>();
    int localID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentData();
        bindData();
        setBtnBack();
        loadFoodReviews(foodID);

        //tăng số lượng và cập nhật tổng tiền
        setBtnPlus();

        //giảm số lượng và cập nhật tổng tiền
        setBtnMinus();

        //thêm vào giỏ hàng
        setBtnOrder();

        //đi tới giỏ hàng
        setBtnCartFragment();
    }

    public void getIntentData() {
        Intent intent = getIntent();
        foodID = intent.getIntExtra("MenuItemID", -1);
        foodName = intent.getStringExtra("MenuItemName");
        foodPrice = intent.getDoubleExtra("MenuItemPrice", 0);
        foodQuantity = intent.getStringExtra("MenuItemQuantity");
        foodImageUrl = intent.getStringExtra("MenuItemImageUrl");
        foodDescription = intent.getStringExtra("MenuItemDescription");
    }

    public void setBtnBack() {
        binding.btnBack.setOnClickListener(v -> finish());
    }
    public void bindData() {
        //số lượng, mặc định là 1
        quantity = (foodQuantity == null) ? 1 : Integer.parseInt(foodQuantity);
        binding.quantity.setText(String.valueOf(quantity));

        //tổng tiền
        String Total = formatCurrency(quantity * foodPrice) + "đ";
        binding.total.setText(Total);

        //tên món
        binding.detailsFoodName.setText(foodName);

        //đơn giá
        binding.detailsFoodPrice.setText(formatCurrency(foodPrice) + "đ");

        //hình ảnh
        Glide.with(this).load(foodImageUrl).into(binding.detailsFoodImage);

        //mô tả món ăn
        binding.detailsFoodDescription.setText(foodDescription);

        txtRatingAvg = binding.txtRatingAvg;
        recyclerView = binding.recyclerReviews;
        initAdapter();
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(reviewAdapter);
    }

    private void updateReviewList(List<Review> reviews) {
        reviewList.clear();
        reviewList.addAll(reviews);
        Log.d("REVIEW", "Số review nhận được: " + reviews.size());
        reviewAdapter.notifyDataSetChanged();
    }

    private void loadFoodReviews(int id) {


        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Log.d("API", "Gọi API lấy đánh giá cho món ăn ID: " + foodID);
        Call<List<Review>> call = apiService.getReviewsByFoodId(String.valueOf(foodID));


        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateReviewList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(UserDetailsActivity.this, "Không tải được đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setBtnPlus() {
        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            binding.quantity.setText(String.valueOf(quantity));
            String total = formatCurrency(quantity * foodPrice) + "đ";
            binding.total.setText(total);

            Log.d("localID: ", String.valueOf(localID));
            // Cập nhật vào Room
            new Thread(() -> {
                CartLocalDb db = CartLocalDb.getInstance(getApplicationContext());
                FoodItem item = db.cartItemDao().findByLocalId(localID);
                if (item != null) {
                    item.setItemQuantity(String.valueOf(quantity));
                    db.cartItemDao().update(item);
                }
            }).start();
        });
    }

    public void setBtnMinus() {
        binding.btnMinus.setOnClickListener(v -> {
            if(quantity > 1) {
                quantity--;

                binding.quantity.setText(String.valueOf(quantity));
                String total = formatCurrency(quantity * foodPrice) + "đ";
                binding.total.setText(total);

            }
        });
    }

    public void setBtnOrder() {
        binding.btnOrder.setOnClickListener(v -> {
            String note = (binding.textInput.getEditText() != null && binding.textInput.getEditText().getText() != null)
                    ? binding.textInput.getEditText().getText().toString()
                    : "";
            Log.d(TAG, "Note: " + note);

            FoodItem newItem = new FoodItem(foodID, foodName, "", foodPrice, foodImageUrl, 1, "", 5, note, String.valueOf(quantity));

            UserCartManager.getInstance().addItem(this, newItem, itemWithId -> {
                localID = itemWithId.getLocalId();
                Log.d(TAG, "localID: " + localID);
            });

            binding.textInput.getEditText().setText("");
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }

    public void setBtnCartFragment() {
        binding.cartFragment.setOnClickListener(v -> {
            binding.cartFragment.setEnabled(false);
            Intent intent = new Intent(this, UserCartActivity.class);
            startActivity(intent);
            // Bật lại sau 500ms
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.cartFragment.setEnabled(true);
            }, 500);
        });
    }
    private static String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }
}