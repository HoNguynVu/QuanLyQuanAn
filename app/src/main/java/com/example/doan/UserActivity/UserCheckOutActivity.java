package com.example.doan.UserActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan.Adapter.UserCheckOutAdapter;
import com.example.doan.DatabaseClass.CurrentUser;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.User.JsonUtils;
import com.example.doan.User.UserCartManager;
import com.example.doan.User.UserConstants;
import com.example.doan.User.UserDataSendRequest;
import com.example.doan.databinding.UserActivityCheckOutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                            try {
                                int orderId = response.getInt("order_id"); // 🔴 LẤY orderId từ JSON
                                int userId = CurrentUser.getInstance().getId();

                                // Gọi API tạo thông báo
                                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                                apiService.createNotification(
                                        userId,
                                        orderId,
                                        "Đặt hàng thành công",
                                        "Đơn hàng với mã số #SE" + orderId + " của bạn đã được ghi nhận."
                                ).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("NOTIFY", "Thông báo đã được lưu");
                                        } else {
                                            Log.e("NOTIFY", "Lỗi khi lưu thông báo");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.e("NOTIFY", "Lỗi kết nối: " + t.getMessage());
                                    }
                                });

                            } catch (JSONException e) {
                                Log.e("Order", "Lỗi phân tích JSON: " + e.getMessage());
                            }
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