package com.example.doan.User;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan.DatabaseClassResponse.OrderRequest;
import com.example.doan.DatabaseClassResponse.OrderResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
