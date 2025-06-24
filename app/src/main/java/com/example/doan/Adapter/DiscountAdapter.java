package com.example.doan.Adapter;

import android.graphics.Color;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doan.DatabaseClass.Discount;
import com.example.doan.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {

    private List<Discount> discountList;
    private OnDiscountListener listener;

    public interface OnDiscountListener {
        void onEdit(Discount discount);
        void onDelete(Discount discount);
    }

    public DiscountAdapter(List<Discount> discountList, OnDiscountListener listener) {
        this.discountList = discountList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_discount, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        holder.txtCode.setText("Mã: " + discount.getCode());
        holder.txtPercent.setText("Giảm: " + discount.getDiscountPercent() + "%");
        holder.txtMaxAmount.setText("Tối đa: " + discount.getMaxDiscountAmount() + " VND");
        holder.txtValid.setText("Từ: " + sdf.format(discount.getValidFrom()) + " đến " + sdf.format(discount.getValidTo()));

        if (discount.isActive()) {
            holder.txtStatus.setText("Trạng thái: Hoạt động");
            holder.txtStatus.setTextColor(Color.parseColor("#4CAF50")); // Xanh lá
        } else {
            holder.txtStatus.setText("Trạng thái: Ngừng");
            holder.txtStatus.setTextColor(Color.parseColor("#F44336")); // Đỏ
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(discount);
            }
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(discount);
            }
        });
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCode, txtPercent, txtMaxAmount, txtValid, txtStatus;

        MaterialButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCode = itemView.findViewById(R.id.txtCode);
            txtPercent = itemView.findViewById(R.id.txtPercent);
            txtMaxAmount = itemView.findViewById(R.id.txtMaxAmount);
            txtValid = itemView.findViewById(R.id.txtValid);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
