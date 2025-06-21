package com.example.doan.AdminActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.MessageAdapter;
import com.example.doan.DatabaseClass.Message;
import com.example.doan.DatabaseClassRequest.MessageRequest;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private int adminId; // ví dụ ID admin
    private int userId;
    private String userName;

    TextView txtUserName;
    TextView txtUserStatus;

    private RecyclerView recyclerView;
    private EditText edtMessage;
    private Button btnSend;
    ImageButton btnBack;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();

    private Handler handler = new Handler();
    private Runnable refreshTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        loadMessages();

        initClick();

        startAutoRefresh();
    }

    public void init()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        adminId = sharedPreferences.getInt("id", -1);
        userId = getIntent().getIntExtra("user_id", -1);
        userName = getIntent().getStringExtra("user_name");
        String receiverRole = getIntent().getStringExtra("user_role"); // 👈 lấy role

        txtUserName = findViewById(R.id.txtUserName);
        txtUserStatus = findViewById(R.id.txtUserStatus);
        txtUserName.setText(userName);
        txtUserStatus.setText("Đang hoạt động");

        recyclerView = findViewById(R.id.recyclerViewMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        adapter = new MessageAdapter(messages, adminId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void initClick()
    {
        btnSend.setOnClickListener(v -> {
            String msg = edtMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                sendMessage(msg);
                edtMessage.setText("");
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void sendMessage(String msg) {

        MessageRequest request = new MessageRequest(adminId, userId, msg);
        APIService api = RetrofitClient.getRetrofitInstance().create(APIService.class);

        api.sendMessage(request).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body().get("success"))) {
                    loadMessages(); // refresh sau khi gửi
                } else {
                    Toast.makeText(ChatActivity.this, "Gửi tin nhắn thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadMessages() {
        APIService api = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Log.d("chat", adminId + " " + userId);
        api.getMessages(adminId, userId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messages.clear();
                    messages.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1); // Cuộn xuống dòng cuối
                    SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("last_read_" + userId, getCurrentTimestamp());
                    editor.apply();

                } else {
                    Toast.makeText(ChatActivity.this, "Không tải được tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void startAutoRefresh() {
        refreshTask = () -> {
            loadMessages();
            handler.postDelayed(refreshTask, 2000); // Cập nhật mỗi 2s
        };
        handler.post(refreshTask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshTask);
    }
}

