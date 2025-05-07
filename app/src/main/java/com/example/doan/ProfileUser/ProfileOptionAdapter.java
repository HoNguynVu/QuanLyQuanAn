package com.example.doan.ProfileUser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

import java.util.List;

public class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionAdapter.ProfileViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ProfileOption option);
    }

    private List<ProfileOption> optionList;
    private OnItemClickListener listener;

    public ProfileOptionAdapter(List<ProfileOption> optionList, OnItemClickListener listener) {
        this.optionList = optionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_option_item, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        ProfileOption option = optionList.get(position);
        holder.optionText.setText(option.getTitle());
        holder.optionIcon.setImageResource(option.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(option);
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView optionIcon;
        TextView optionText;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            optionIcon = itemView.findViewById(R.id.optionIcon);
            optionText = itemView.findViewById(R.id.optionText);
        }
    }
}
