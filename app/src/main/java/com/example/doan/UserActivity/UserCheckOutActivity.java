package com.example.doan.UserActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doan.DatabaseClass.Discount;
import com.example.doan.DatabaseClassResponse.DiscountResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.Adapter.UserCheckOutAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClassRequest.OrderRequest;
import com.example.doan.DatabaseClassResponse.OrderResponse;
import com.example.doan.R;
import com.example.doan.User.OrderUtils;
import com.example.doan.User.UserCartManager;
import com.example.doan.databinding.UserActivityCheckOutBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCheckOutActivity extends AppCompatActivity {

    private List<FoodItem> cartList;
    private UserCheckOutAdapter adapter;
    private UserActivityCheckOutBinding binding;
    private EditText Address, DiscountCode;

    private int discountAmount = 0;
    private double discountTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartList = UserCartManager.getInstance().getCartItems();

        init();

        setTvApplyDiscount();
        setRecyclerView();
        setBtnBack();
        setPrice();
        setBtnCheckOut();
    }

    private void init()
    {
        Address = binding.edtAddress;
        DiscountCode = binding.edtDiscount;
    }

    private void setTvApplyDiscount() {
        binding.tvApplyDiscount.setOnClickListener(v -> {
            String discount = DiscountCode.getText().toString().trim();

            if (discount.isEmpty()) {
                DiscountCode.setError("Vui lòng nhập mã giảm giá");
                discountAmount=0;
                setPrice();
                return;
            }

            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<DiscountResponse> call = apiService.checkDiscount(discount);

            call.enqueue(new Callback<DiscountResponse>() {
                @Override
                public void onResponse(Call<DiscountResponse> call, Response<DiscountResponse> response) {
                    if (response.body().isSuccess() && response.body() != null) {
                        // Kiểm tra kết quả từ API
                        DiscountResponse discountResponse = response.body();
                        Discount discount = discountResponse.getDiscount();
                        discountAmount = discount.getDiscountPercent();
                        discountTotal = discount.getMaxDiscountAmount();
                        setPrice();

                    } else {
                        DiscountCode.setError("Mã giảm giá không hợp lệ hoặc đã hết hạn");
                        discountAmount=0;
                        setPrice();
                    }
                }

                @Override
                public void onFailure(Call<DiscountResponse> call, Throwable t) {
                    DiscountCode.setError("Lỗi mạng: " + t.getMessage());
                    setPrice();
                }
            });
        });
    }


    private void setBtnBack() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void setPrice() {
        double total = UserCartManager.getInstance().getTotalOrder();
        double discount = 0;
        double finalTotal = total;

        if (discountAmount > 0) {
            discount = total * discountAmount / 100;
            if (discount > discountTotal) {
                discount = discountTotal;
            }
            finalTotal = total - discount;
        }

        // Hiển thị số nguyên nếu bạn không cần số lẻ
        binding.price.setText(String.format("%.0f", total)); // Giá gốc
        binding.fee.setText("0");
        binding.totalOrder.setText(String.format("%.0f", finalTotal)); // Tổng sau giảm

        // Nếu bạn muốn hiển thị phép tính như: "100 - 10% = 90"
        String priceCalcText;
        if (discountAmount > 0) {
            priceCalcText = String.format("%.0f - %.0f%% = %.0f", total, (double) discountAmount, finalTotal);
            binding.price.setText(priceCalcText);

        } else {

        }

    }


    private void setRecyclerView() {
        adapter = new UserCheckOutAdapter(cartList, this);
        binding.recyclerviewList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
        );

        binding.recyclerviewList.addItemDecoration(dividerItemDecoration);
    }

    private void setBtnCheckOut() {
        binding.btnCheckout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            int userID = sharedPreferences.getInt("id", 0);
            OrderRequest orderRequest = new OrderRequest();
            if (Address.getText().toString().isEmpty()) {
                binding.edtAddress.setError("Vui lòng nhập địa chỉ");
                return;
            }
            orderRequest.userId = userID;
            orderRequest.items = OrderUtils.convertFoodItemsToOrderItems(cartList);
            String discount = DiscountCode.getText().toString().trim();
            orderRequest.discountCode = discount.isEmpty() ? null : discount; // Có thể cập nhật khi có ô nhập mã giảm giá
            orderRequest.paymentMethod = "cash";
            orderRequest.address = Address.getText().toString();


            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<OrderResponse> call = apiService.createOrder(orderRequest);
            Gson gson = new Gson();
            String json = gson.toJson(orderRequest);
            Log.d("OrderJSON", json);

            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        OrderResponse orderResponse = response.body();
                        Log.d("Order", "Đặt hàng thành công với ID: " + orderResponse.getOrderId());

                        // Xóa giỏ hàng và chuyển màn hình
                        UserCartManager.getInstance().cleanCart(UserCheckOutActivity.this);
                        Intent intent = new Intent(UserCheckOutActivity.this, UserOrderSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(UserCheckOutActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                        Log.e("Order", "Response không hợp lệ hoặc body null");
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    Toast.makeText(UserCheckOutActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Order", "onFailure: " + t.getMessage());
                }
            });
        });
    }
}
