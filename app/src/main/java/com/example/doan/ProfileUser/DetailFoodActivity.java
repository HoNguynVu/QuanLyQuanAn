package com.example.doan.ProfileUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.doan.Adapter.ReviewAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.Review;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFoodActivity extends AppCompatActivity {
    private ImageView imgFood;
    private TextView txtName, txtPrice, txtDescription;
    private ListView listReviews;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList = new ArrayList<>();

    private String foodId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_food);
        imgFood = findViewById(R.id.img_food_detail);
        txtName = findViewById(R.id.txt_food_name_detail);
        txtPrice = findViewById(R.id.txt_food_price_detail);
        txtDescription = findViewById(R.id.txt_food_description);
        listReviews = findViewById(R.id.list_reviews);

        reviewAdapter = new ReviewAdapter(this, reviewList);
        listReviews.setAdapter(reviewAdapter);


        foodId = getIntent().getStringExtra("id");

        getFoodById(foodId);
        // Gọi API lấy danh sách review
        getReviewsByFoodId(foodId);
    }
    private void getFoodById(String foodId) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        Call<FoodItem> call = apiService.getFoodByID(foodId);
        call.enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FoodItem food = response.body();

                    Log.d("API", "Tên món: " + food.getName());
                    txtName.setText(food.getName());
                    txtPrice.setText(food.getPrice() + "đ");
                    txtDescription.setText(food.getDescription());
                    Glide.with(DetailFoodActivity.this).load(food.getImageUrl()).into(imgFood);
                } else {
                    Log.e("API", "Không tìm thấy món ăn với ID: " + foodId);
                }
            }

            @Override
            public void onFailure(Call<FoodItem> call, Throwable t) {
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }


    private void getReviewsByFoodId(String foodId) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Review>> call = apiService.getReviewsByFoodId(foodId);

        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList.clear();
                    reviewList.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(DetailFoodActivity.this, "Không tải được đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }
}