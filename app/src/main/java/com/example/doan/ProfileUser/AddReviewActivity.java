package com.example.doan.ProfileUser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReviewActivity extends AppCompatActivity {
    private ImageView imgFood;
    private TextView tvFoodName;
    private TextInputEditText edtComment;
    private Button btnSubmit, btnCancel;

    private RatingBar ratingBarUser;

    private int foodId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_review);

        initView();
        loadData();
        setClick();
    }

    private void initView()
    {
        imgFood = findViewById(R.id.imgFood);
        tvFoodName = findViewById(R.id.tvFoodName);
        edtComment = findViewById(R.id.edtComment);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        ratingBarUser = findViewById(R.id.ratingBarUser);
    }

    private void loadData()
    {
        foodId = getIntent().getIntExtra("food_id", 0);
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<FoodItem> call = apiService.getFoodByID(String.valueOf(foodId));
        call.enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bindFoodData(response.body());
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

    private void bindFoodData(FoodItem food) {
        tvFoodName.setText(food.getName());
        Glide.with(this).load(food.getImage_url()).into(imgFood);
    }

    private void  setClick()
    {
        btnSubmit.setOnClickListener(v -> {
            btnSubmit.setEnabled(false);
            String comment = edtComment.getText().toString();
            int rating = (int)ratingBarUser.getRating();
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
            int userID = sharedPreferences.getInt("id",-1);

            addReview(comment, rating , userID);
            setResult(RESULT_OK); // báo về cho Activity gọi nó
            finish();
        });
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }


    private void addReview(String comment, int rating , int userID) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<ResponseBody> call = apiService.addReview(foodId, userID, comment, rating);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        JSONObject obj = new JSONObject(json);
                        String status = obj.getString("status");
                        String message = obj.getString("message");

                        Toast.makeText(AddReviewActivity.this, message, Toast.LENGTH_SHORT).show();

                        if ("success".equals(status)) {
                            setResult(RESULT_OK);
                            finish();
                        }

                    } catch (Exception e) {
                        Toast.makeText(AddReviewActivity.this, "Lỗi khi đọc phản hồi từ server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddReviewActivity.this, "Phản hồi không hợp lệ từ server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddReviewActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}