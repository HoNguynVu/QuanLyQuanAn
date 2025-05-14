package com.example.doan.Network;

import com.example.doan.DatabaseClass.GenericResponse;
import com.example.doan.DatabaseClass.LoginResponse;
import com.example.doan.DatabaseClass.Order;
import com.example.doan.DatabaseClass.StatisticsResponse;
import com.example.doan.DatabaseClass.StatusResponse;
import com.example.doan.DatabaseClass.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @FormUrlEncoded
    @POST("get_user_info.php")  // file PHP bạn cần tạo
    Call<User> getUserInfo(@Field("email") String email);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("signup.php")
    Call<GenericResponse> register(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("date_birth") String dob,
            @Field("role") String role
    );


    @FormUrlEncoded
    @POST("verify_otp.php")
    Call<GenericResponse> verifyOtp(@Field("otp") String otp);

    @POST("get_statistics.php")
    Call<StatisticsResponse> getStatistics();

    @GET("get_orders.php")
    Call<List<Order>> getOrders();

    @FormUrlEncoded
    @POST("update_order_status.php")
    Call<StatusResponse> updateOrderStatus(
            @Field("order_id") int orderId,
            @Field("status") String status
    );


}
