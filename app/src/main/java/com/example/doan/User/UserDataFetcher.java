package com.example.doan.User;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.FoodListResponse;
import com.example.doan.DatabaseClass.Order;
import com.example.doan.DatabaseClass.OrderItem;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;

public class UserDataFetcher {
    public interface FetchCallBack<T> {
        void onSuccess(List<T> data);
        void onError(String message);
    }

    public static void fetchFoods(FetchCallBack<FoodItem> callBack) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<FoodListResponse> call = apiService.getFoodsByCategory("all");

        call.enqueue(new retrofit2.Callback<FoodListResponse>() {
            @Override
            public void onResponse(Call<FoodListResponse> call, retrofit2.Response<FoodListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callBack.onSuccess(response.body().data);
                } else {
                    callBack.onError("Không lấy được dữ liệu món ăn");
                }
            }

            @Override
            public void onFailure(Call<FoodListResponse> call, Throwable t) {
                callBack.onError(t.getMessage());
            }
        });
    }

}
