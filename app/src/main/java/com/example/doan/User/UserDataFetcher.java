package com.example.doan.User;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClassResponse.FoodListResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserDataFetcher {
    public interface FetchCallBack {
        void onSuccess(List<FoodItem> data);
        void onError(VolleyError error);
    }

    public static void fetchFoods(Context context, String url, FetchCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("foods", response);

                    // Dùng FoodListResponse thay vì List<FoodItem>
                    FoodListResponse foodResponse = new Gson().fromJson(response, FoodListResponse.class);
                    if (foodResponse != null && "success".equalsIgnoreCase(foodResponse.status) && foodResponse.data != null) {
                        callBack.onSuccess(foodResponse.data);
                    } else {
                        callBack.onSuccess(new ArrayList<>()); // trả về list rỗng thay vì null
                    }

                },
                error -> {
                    Log.e("fetchFoods", "Volley error", error);
                    callBack.onError(error);
                });

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(stringRequest);
    }

}
