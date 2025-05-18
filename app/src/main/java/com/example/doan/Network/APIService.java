package com.example.doan.Network;

import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.FoodListResponse;
import com.example.doan.DatabaseClass.GenericResponse;
import com.example.doan.DatabaseClass.LoginResponse;
import com.example.doan.DatabaseClass.Order;
import com.example.doan.DatabaseClass.StatisticsResponse;
import com.example.doan.DatabaseClass.StatusResponse;
import com.example.doan.DatabaseClass.UploadResponse;
import com.example.doan.DatabaseClass.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<GenericResponse> sendResetOtp(@Field("email") String email);

    @FormUrlEncoded
    @POST("verify_reset_otp.php")
    Call<GenericResponse> verifyResetOtp(
            @Field("email") String email,
            @Field("otp") String otp
    );

    @FormUrlEncoded
    @POST("reset_password.php")
    Call<GenericResponse> resetPassword(
            @Field("email") String email,
            @Field("new_password") String newPassword
    );

    @GET("get_foods.php")
    Call<FoodListResponse> getFoodsByCategory(@Query("category") String category);

    @FormUrlEncoded
    @POST("delete_food.php")
    Call<GenericResponse> deleteFood(@Field("id") int id);

    @FormUrlEncoded
    @POST("add_food.php")
    Call<GenericResponse> addFood(
            @Field("name") String name,
            @Field("price") double price,
            @Field("category") String category,
            @Field("image_url") String imageUrl
    );

    @FormUrlEncoded
    @POST("upload_image.php")
    Call<UploadResponse> uploadImage(
            @Field("image") String base64Image
    );

    @FormUrlEncoded
    @POST("update_food.php")
    Call<GenericResponse> updateFood(
            @Field("id") int id,
            @Field("name") String name,
            @Field("price") double price,
            @Field("category") String category,
            @Field("image_url") String imageUrl
    );



    @FormUrlEncoded
    @POST("get_admin_info.php")
    Call<User> getAdminInfo(@Field("email") String email);

    @FormUrlEncoded
    @POST("update_admin_info.php")
    Call<GenericResponse> updateAdminInfo(
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("date_birth") String dob
    );

    @FormUrlEncoded
    @POST("change_password.php")
    Call<GenericResponse> changePassword(
            @Field("email") String email,
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword
    );


}
