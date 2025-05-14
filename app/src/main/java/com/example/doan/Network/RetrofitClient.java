package com.example.doan.Network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Gson cho phép phân tích lenient nếu JSON không chuẩn tuyệt đối
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Interceptor để in toàn bộ dữ liệu phản hồi từ server
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request request = chain.request();
                        Response response = chain.proceed(request);

                        ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                        Log.d("HTTP_RESPONSE_BODY", responseBodyCopy.string());

                        return response;
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8081/restaurantapi/") // thay bằng IP LAN nếu dùng thiết bị thật
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}