package com.example.doan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.doan.DatabaseClass.Review;
import com.example.doan.R;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {
    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        }

        Review review = reviews.get(position);

        TextView txtUser = convertView.findViewById(R.id.txt_review_user);
        TextView txtRating = convertView.findViewById(R.id.txt_review_rating);
        TextView txtComment = convertView.findViewById(R.id.txt_review_comment);

        txtUser.setText("Người dùng: " + review.getUserId()); // bạn có thể thay bằng tên nếu có
        txtRating.setText("★ " + review.getRating());
        txtComment.setText(review.getComment());

        return convertView;
    }
}
