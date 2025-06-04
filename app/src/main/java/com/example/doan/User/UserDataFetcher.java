package com.example.doan.User;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.OrderItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UserDataFetcher {
    public interface FetchCallBack<T> {
        void onSuccess(List<T> data);
        void onError(VolleyError error);
    }

    public static void fetchFoods(Context context, String url, FetchCallBack<FoodItem> callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Type itemListType = new TypeToken<List<FoodItem>>(){}.getType();
                    List<FoodItem> newList = new Gson().fromJson(response, itemListType);
                    Log.d("foods", response);
                    callBack.onSuccess(newList);
                },
                error ->  {
                });

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(stringRequest);
    }

    public static void FetchOrderItems(Context context, String url, FetchCallBack<OrderItem> callBack) {
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {

                },
                error -> {

                });
    }
}
