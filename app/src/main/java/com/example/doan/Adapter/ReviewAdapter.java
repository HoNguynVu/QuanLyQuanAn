package com.example.doan.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.Review;
import com.example.doan.DatabaseClass.User;
import com.example.doan.DatabaseClassResponse.UserResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        // Gán trước comment và rating (có sẵn)
        holder.txtRating.setText("★ " + review.getRating());
        holder.txtComment.setText(review.getComment());

        // Hiện tạm ID người dùng trong khi chờ API
        holder.txtUser.setText("Người dùng: " + review.getUserId());

        // Gọi API lấy thông tin user
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<UserResponse> call = apiService.getUserByID(String.valueOf(review.getUserId()));

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body().user;
                    holder.txtUser.setText("Người dùng: " + user.getName());
                    Log.d("USER", "Người dùng: " + user.getName());
                } else {
                    holder.txtUser.setText("Người dùng: (không tìm thấy)");
                    Log.e("USER", "Không lấy được người dùng hoặc response không hợp lệ");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                holder.txtUser.setText("Người dùng: (lỗi mạng)");
                Log.e("USER", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView txtUser, txtRating, txtComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txt_review_user);
            txtRating = itemView.findViewById(R.id.txt_review_rating);
            txtComment = itemView.findViewById(R.id.txt_review_comment);
        }
    }
}
