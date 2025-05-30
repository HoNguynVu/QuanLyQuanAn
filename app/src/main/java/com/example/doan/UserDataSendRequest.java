package com.example.doan;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDataSendRequest {

    private final RequestQueue requestQueue;
    private String url;

    public interface RespondListener {
        void onSuccess(JSONObject response);

        void onError(String error);
    }

    public UserDataSendRequest(Context context, String url) {
        this.url = url;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void sendCreateOrderRequest(int userID, JSONArray items, String discountCode, String paymentMethod, RespondListener listener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("user_id", userID);
            requestBody.put("items", items);
            if (discountCode != null)
                requestBody.put("discount_code", discountCode);
            if (paymentMethod != null)
                requestBody.put("payment_method", paymentMethod);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError("Lỗi tạo JSON request: " + e.getMessage());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response: " + response.toString());
                        listener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        listener.onError("Lỗi kết nối: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }


}
