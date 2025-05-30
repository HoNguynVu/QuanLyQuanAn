package com.example.doan;

import android.util.Log;

import com.example.doan.DatabaseClass.FoodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonUtils {
    public static JSONArray foodItemListToJsonArray(List<FoodItem> foodItemList) {
        JSONArray jsonArray = new JSONArray();
        for (FoodItem item : foodItemList) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("food_id", item.getId());
                Log.d("FoodID", String.valueOf(item.getId()));
                int qty = 1;
                try {
                    qty = Integer.parseInt(item.getItemQuantity());
                } catch (Exception e) { /* fallback */ }
                obj.put("quantity", qty);
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }
}
