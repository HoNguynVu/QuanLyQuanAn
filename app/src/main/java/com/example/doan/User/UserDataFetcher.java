package com.example.doan.User;

import android.util.Log;

import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClassResponse.FoodListResponse;
import com.example.doan.DatabaseClassResponse.PopularFoodResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserDataFetcher {
    public interface FetchCallBack<T> {
        void onSuccess(List<T> data);
        void onError(String message);
    }

    public static void fetchFoods(FetchCallBack<FoodItem> callBack, String category) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<FoodListResponse> call = apiService.getFoodsByCategory(category);

        call.enqueue(new retrofit2.Callback<FoodListResponse>() {
            @Override
            public void onResponse(Call<FoodListResponse> call, retrofit2.Response<FoodListResponse> response) {
                Log.d("Response", response.toString());
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

    public static void fetchPopularFoods(FetchCallBack<FoodItem> callBack) {
        APIService api = RetrofitClient.getRetrofitInstance().create(APIService.class);
        api.getPopularFoods().enqueue(new retrofit2.Callback<PopularFoodResponse>() {

            @Override
            public void onResponse(Call<PopularFoodResponse> call, Response<PopularFoodResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    callBack.onSuccess(response.body().data);
                }
                else {
                    callBack.onError("Không lấy được dữ liệu món ăn");
                }
            }

            @Override
            public void onFailure(Call<PopularFoodResponse> call, Throwable t) {
                callBack.onError(t.getMessage());
            }
        });
    }

}
