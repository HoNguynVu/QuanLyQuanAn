package com.example.doan.User;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan.DatabaseClass.FoodItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UserDataFetcher {
    public interface FetchCallBack {
        void onSuccess(List<FoodItem> data);
        void onError(VolleyError error);
    }

    public static void fetchFoods(Context context, String url, FetchCallBack callBack) {
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
}
