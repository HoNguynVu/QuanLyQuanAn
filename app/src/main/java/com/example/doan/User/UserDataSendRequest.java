package com.example.doan.User;

import com.example.doan.DatabaseClassRequest.OrderRequest;
import com.example.doan.DatabaseClassResponse.OrderResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class UserDataSendRequest {

    public interface RespondListener {
        void onSuccess(OrderResponse response);

        void onError(String error);
    }

    public static void sendCreateOrderRequest(int userID, List<OrderRequest.Item> items, String discountCode,
                                              String paymentMethod, RespondListener listener) {
        OrderRequest request = new OrderRequest();
        request.userId = userID;
        request.items = items;
        request.discountCode = discountCode;
        request.paymentMethod = paymentMethod;

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<OrderResponse> call = apiService.createOrder(request);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, retrofit2.Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Lỗi server hoặc không có phản hồi hợp lệ");
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                listener.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }


}
