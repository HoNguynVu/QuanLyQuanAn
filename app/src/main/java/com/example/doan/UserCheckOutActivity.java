package com.example.doan;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan.Adapter.UserCheckOutAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.databinding.UserActivityCheckOutBinding;

import org.json.JSONArray;
import org.json.JSONObject;

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
            JSONArray itemsJSON = JsonUtils.foodItemListToJsonArray(cartList);
            UserDataSendRequest request = new UserDataSendRequest(this, UserConstants.CREATE_ORDER_URL);
            request.sendCreateOrderRequest(
                    1,
                    itemsJSON,
                    null,
                    "cash",
                    new UserDataSendRequest.RespondListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d("Order", "Thành công: " + response.toString());
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("Order", "Lỗi: " + error);
                        }
                    }
            );
            Intent intent = new Intent(UserCheckOutActivity.this, UserOrderSuccessActivity.class);
            startActivity(intent);
        });
    }
}