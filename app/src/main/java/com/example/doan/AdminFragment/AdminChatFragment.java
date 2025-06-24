package com.example.doan.AdminFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan.AdminActivity.ChatActivity;
import com.example.doan.Adapter.UserChatAdapter;
import com.example.doan.DatabaseClass.User;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminChatFragment extends Fragment {
    private int adminId;
    private RecyclerView recyclerViewUsers;
    private List<User> userList;
    private List<User> allUsers = new ArrayList<>(); // Danh sách không đổi để filter

    private UserChatAdapter userAdapter;

    private EditText edtChatSearch;
    private Runnable refreshTask; // Task để tự động làm mới
    private Handler handler = new Handler();

    public AdminChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);

        init(view);
        loadUsers();
        searchUsers();
        startAutoRefresh();
        return view;
    }



    private void init(View view)
    {
        edtChatSearch = view.findViewById(R.id.etChatSearch);

        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        allUsers = new ArrayList<>();

        userAdapter = new UserChatAdapter(userList, getContext(), user -> {
            MarkMessagesAsRead(user.getId(), adminId);
            // 2. Cập nhật trạng thái trong list
            user.setHasUnread(false);
            userAdapter.notifyDataSetChanged(); // 👈 bắt buộc để đổi màu
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("user_id", user.getId());
            intent.putExtra("user_name", user.getName());
            startActivity(intent);
        });

        recyclerViewUsers.setAdapter(userAdapter);
    }

    private void MarkMessagesAsRead(int senderId, int receiverId) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        apiService.markMessagesAsRead(senderId, receiverId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d("CHAT", "Marked messages as read");
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("CHAT", "Failed to mark messages as read: " + t.getMessage());
            }
        });
    }

    private void searchUsers() {
        edtChatSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUserList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void filterUserList(String query) {
        List<User> filteredList = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        userAdapter.updateList(filteredList);
    }

    private void loadUsers() {
        SharedPreferences prefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        adminId = prefs.getInt("id", -1);

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        apiService.getAllUsers(adminId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", new Gson().toJson(response.body()));

                    allUsers.clear();
                    userList.clear();

                    for (User user : response.body()) {
                        if (user.getId() == adminId) continue;
                        allUsers.add(user);
                    }

                    // 🔽 Sắp xếp theo last_message_time
                    Collections.sort(allUsers, (u1, u2) -> {
                        String t1 = u1.getLastMessageTime();
                        String t2 = u2.getLastMessageTime();
                        if (t1 == null && t2 == null) return 0;
                        if (t1 == null) return 1;
                        if (t2 == null) return -1;
                        return t2.compareTo(t1); // mới nhất trước
                    });

                    userList.addAll(allUsers);
                    userAdapter.updateList(userList);

                    for (User u : allUsers) {
                        Log.d("SORT_DEBUG", u.getName() + " - " + u.getLastMessageTime());
                    }


                } else {
                    Toast.makeText(getContext(), "Không tải được danh sách người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startAutoRefresh() {
        refreshTask = () -> {
            loadUsers();
            handler.postDelayed(refreshTask, 2000); // Cập nhật mỗi 2s
        };
        handler.post(refreshTask);
    }
    @Override
    public void onDestroyView() //gọi dtroy để hủy task, tránh rò rỉ bộ nhớ
    {
        super.onDestroyView();
        if (handler != null && refreshTask != null) {
            handler.removeCallbacks(refreshTask);
        }
    }


}
