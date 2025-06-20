package com.example.doan.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.DatabaseClass.User;
import com.example.doan.R;

import java.util.List;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.UserViewHolder> {

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    private List<User> userList;
    private Context context;
    private OnUserClickListener listener;

    public UserChatAdapter(List<User> userList, Context context, OnUserClickListener listener) {
        this.userList = userList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.role.setText(user.getRole());

        // Đổi màu nếu là Admin
        if ("Admin".equalsIgnoreCase(user.getRole())) {
            holder.role.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.role.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        }

        if (user.isHasUnread()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E0F7FA")); // Màu khi có tin nhắn chưa đọc
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE); // Bình thường
        }

        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, role;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            email = itemView.findViewById(R.id.txtEmail);
            role = itemView.findViewById(R.id.txtRole);
        }
    }

    public void updateList(List<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

}
