package com.example.doan.Adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

import java.util.List;

public class PopularItemAdapter extends RecyclerView.Adapter<PopularItemAdapter.myViewHolder> {

    List<String> title;
    List<String> price;
    List<Integer> image;
    LayoutInflater inflater;
    Activity activity;

    public PopularItemAdapter(Context context, List<String> title, List<String> price, List<Integer> image, Activity activity) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.activity = activity;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.popular_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.title.setText(title.get(position));
        holder.price.setText(price.get(position));
        holder.imageView.setImageResource(image.get(position));
        holder.button.setOnClickListener(v ->  {
            Toast.makeText(v.getContext(), "clicked!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        ImageView imageView;
        LinearLayout linearLayout;
        Button button;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.tv_name);
            this.price = itemView.findViewById(R.id.tv_price);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.linearLayout = itemView.findViewById(R.id.linearLayout);
            this.button = itemView.findViewById(R.id.btn_add);
        }
    }
}
