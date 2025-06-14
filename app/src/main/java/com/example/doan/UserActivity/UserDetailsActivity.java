package com.example.doan.UserActivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.User.UserCartManager;
import com.example.doan.databinding.UserActivityDetailsBinding;

public class UserDetailsActivity extends AppCompatActivity {
    UserActivityDetailsBinding binding;
    int quantity;
    int foodID;
    String foodName;
    double foodPrice;
    String foodQuantity;
    String foodImageUrl;
    String foodDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentData();
        bindData();
        setBtnBack();

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
        String Total = String.valueOf(foodPrice * quantity);
        binding.total.setText(Total);

        //tên món
        binding.detailsFoodName.setText(foodName);

        //đơn giá
        binding.detailsFoodPrice.setText(String.valueOf(foodPrice));

        //hình ảnh
        Glide.with(this).load(foodImageUrl).into(binding.detailsFoodImage);

        //mô tả món ăn
        binding.detailsFoodDescription.setText(foodDescription);
    }

    public void setBtnPlus() {
        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            binding.quantity.setText(String.valueOf(quantity));
            String total = quantity * foodPrice + "";
            binding.total.setText(total);
        });
    }

    public void setBtnMinus() {
        binding.btnMinus.setOnClickListener(v -> {
            if(quantity > 1) {
                quantity--;

                binding.quantity.setText(String.valueOf(quantity));
                String total = quantity * foodPrice + "";
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
            UserCartManager.getInstance().addItem(new FoodItem(foodID, foodName, "", foodPrice, foodImageUrl, 1, "", 5, note,  String.valueOf(quantity)));
            binding.textInput.getEditText().setText("");
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }

    public void setBtnCartFragment() {
        binding.cartFragment.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserCartActivity.class);
            startActivity(intent);
        });
    }
}