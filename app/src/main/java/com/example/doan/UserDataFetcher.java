package com.example.doan;

import static com.example.doan.UserConstants.GETFOODS_URL;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UserDataFetcher {
    public interface FetchCallBack {
        void onSuccess(List<UserItem> data);
        void onError(VolleyError error);
    }

    public static void fetchFoods(Context context, String url, FetchCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETFOODS_URL,
                response -> {
                    Type itemListType = new TypeToken<List<UserItem>>(){}.getType();
                    List<UserItem> newList = new Gson().fromJson(response, itemListType);
                    callBack.onSuccess(newList);
                },
                error ->  {
                });

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(stringRequest);
    }
}
