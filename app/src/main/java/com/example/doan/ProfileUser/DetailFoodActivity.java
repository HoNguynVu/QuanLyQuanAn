package com.example.doan.ProfileUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.Adapter.ReviewAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.Review;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFoodActivity extends AppCompatActivity {

    private ImageView imgFood;
    private TextView txtName, txtPrice, txtDescription,txtRatingAvg;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private Button btnAddReview, btnBack;
    private ActivityResultLauncher<Intent> addReviewLauncher;

    private List<Review> reviewList = new ArrayList<>();

    private int foodId;
    private String userRole;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Reload lại dữ liệu món ăn
            loadFoodDetails(foodId);
            loadFoodReviews(foodId);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_food);

        initViews();
        setLauncher();
        initAdapter();
        setButton();
        getFoodIdFromIntent();
        loadFoodDetails(foodId);
        loadFoodReviews(foodId);
    }

    // Khởi tạo các view từ layout
    private void initViews() {
        imgFood = findViewById(R.id.img_food_detail);
        txtName = findViewById(R.id.txt_food_name_detail);
        txtPrice = findViewById(R.id.txt_food_price_detail);
        txtDescription = findViewById(R.id.txt_food_description);
        txtRatingAvg = findViewById(R.id.txt_rating_avg);
        btnAddReview = findViewById(R.id.btn_add_review);
        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_reviews);

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userRole = sharedPreferences.getString("role", "User");
    }

    // Khởi tạo adapter cho list đánh giá
    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(reviewAdapter);
    }
    // Đặt sự kiện cho các nút
    private void setButton() {
        btnAddReview.setOnClickListener(v -> {
            if(!userRole.equals("User")) {
                Toast.makeText(this, "Admin không thể thêm đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }
            btnAddReview.setEnabled(false);
            Intent intent = new Intent(DetailFoodActivity.this, AddReviewActivity.class);
            intent.putExtra("food_id", foodId);
            addReviewLauncher.launch(intent);
            btnAddReview.setEnabled(true);
        });
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
    // Lấy ID món ăn từ Intent truyền vào
    private void getFoodIdFromIntent() {
        foodId = getIntent().getIntExtra("id", 0);
    }

    // Gọi API để lấy thông tin chi tiết món ăn
    private void loadFoodDetails(int id) {
        if (id <= 0) {
            showError("Không có ID món ăn");
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<FoodItem> call = apiService.getFoodByID(String.valueOf(foodId));
        call.enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bindFoodData(response.body());
                } else {
                    Log.e("API", "Không tìm thấy món ăn với ID: " + id);
                }
            }

            @Override
            public void onFailure(Call<FoodItem> call, Throwable t) {
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // Hiển thị thông tin món ăn lên UI
    private void bindFoodData(FoodItem food) {
        txtName.setText("Tên món ăn : "+ food.getName());
        txtPrice.setText("Giá món ăn : " +  formatCurrency(food.getPrice())+"đ");
        txtDescription.setText(food.getDescription());
        txtRatingAvg.setText(food.getRatingAvg() + "★");
        Glide.with(this).load(food.getImage_url()).into(imgFood);
    }

    // Gọi API để lấy danh sách đánh giá món ăn
    private void loadFoodReviews(int id) {
        if (id <= 0) {
            showError("Không có ID món ăn để tải đánh giá");
            return;
        }

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Log.d("API", "Gọi API lấy đánh giá cho món ăn ID: " + foodId);
        Call<List<Review>> call = apiService.getReviewsByFoodId(String.valueOf(foodId));


        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateReviewList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                showError("Không tải được đánh giá");
            }
        });
    }

    // Cập nhật danh sách review và thông báo adapter
    private void updateReviewList(List<Review> reviews) {
        reviewList.clear();
        reviewList.addAll(reviews);
        Log.d("REVIEW", "Số review nhận được: " + reviews.size());
        reviewAdapter.notifyDataSetChanged();
    }

    // Hiển thị Toast thông báo lỗi
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setLauncher(){
        addReviewLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadFoodDetails(foodId);
                        loadFoodReviews(foodId);
                    }
                }
        );

    }
    private static String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }
}
