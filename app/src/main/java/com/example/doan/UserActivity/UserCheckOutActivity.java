package com.example.doan.UserActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan.Adapter.UserCheckOutAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClassResponse.OrderRequest;
import com.example.doan.DatabaseClassResponse.OrderResponse;
import com.example.doan.User.OrderUtils;
import com.example.doan.User.UserCartManager;
import com.example.doan.User.UserDataSendRequest;
import com.example.doan.databinding.UserActivityCheckOutBinding;

import java.util.List;

public class UserCheckOutActivity extends AppCompatActivity {
    List<FoodItem> cartList = UserCartManager.getInstance().getCartItems();
    UserCheckOutAdapter adapter;
    private UserActivityCheckOutBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new UserCheckOutAdapter(cartList, this);
        binding.recyclerviewList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
        );

        binding.recyclerviewList.addItemDecoration(dividerItemDecoration);

        binding.btnBack.setOnClickListener(v -> finish());

        String price = UserCartManager.getInstance().getTotalOrder() + "$";
        binding.price.setText(price);
        binding.fee.setText("0$");
        binding.totalOrder.setText(price);

        binding.btnCheckout.setOnClickListener(v -> {
            int userID = 1;
            List<OrderRequest.Item> items = OrderUtils.convertFoodItemsToOrderItems(cartList);
            String discountCode = null; // nếu có
            String paymentMethod = "cash"; // hoặc "momo", "card" tùy bạn
            Log.d("DEBUG", "UserID gửi lên: " + userID);

            UserDataSendRequest.sendCreateOrderRequest(userID, items, discountCode, paymentMethod,
                    new UserDataSendRequest.RespondListener() {
                        @Override
                        public void onSuccess(OrderResponse response) {
                            // Đặt hàng thành công, xử lý kết quả
                            Log.d("Order", "Đặt hàng thành công với ID: " + response.getOrderId());

                            // Chuyển màn
                            UserCartManager.getInstance().deleteItem();
                            Intent intent = new Intent(UserCheckOutActivity.this, UserOrderSuccessActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("Order", "Lỗi khi đặt hàng: " + error);
                        }
                    }
            );
        });
    }
}