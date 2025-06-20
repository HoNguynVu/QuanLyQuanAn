package com.example.doan.Adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.DatabaseClass.Message;
import com.example.doan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;
    private int currentUserId;

    public MessageAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message msg = messages.get(position);

        if (msg.getSender_id() == currentUserId) {
            // Tin nhắn gửi
            holder.sentMessageLayout.setVisibility(View.VISIBLE);
            holder.receivedMessageLayout.setVisibility(View.GONE);

            holder.txtSentMessage.setText(msg.getMessage());
            holder.txtSentTime.setText(formatTime(msg.getTimestamp())); // bạn có thể định dạng thời gian đẹp hơn nếu cần
        } else {
            // Tin nhắn nhận
            holder.sentMessageLayout.setVisibility(View.GONE);
            holder.receivedMessageLayout.setVisibility(View.VISIBLE);

            holder.txtReceivedMessage.setText(msg.getMessage());
            holder.txtReceivedTime.setText(formatTime(msg.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public String formatTime(String rawTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(rawTime);
            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return outFormat.format(date);
        } catch (Exception e) {
            return rawTime; // fallback nếu lỗi
        }
    }


    static class MessageViewHolder extends RecyclerView.ViewHolder {

        LinearLayout sentMessageLayout, receivedMessageLayout;
        TextView txtSentMessage, txtSentTime, txtReceivedMessage, txtReceivedTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            sentMessageLayout = itemView.findViewById(R.id.sentMessageLayout);
            receivedMessageLayout = itemView.findViewById(R.id.receivedMessageLayout);

            txtSentMessage = itemView.findViewById(R.id.txtSentMessage);
            txtSentTime = itemView.findViewById(R.id.txtSentTime);
            txtReceivedMessage = itemView.findViewById(R.id.txtReceivedMessage);
            txtReceivedTime = itemView.findViewById(R.id.txtReceivedTime);
        }
    }
}
