package com.example.doan.UserFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.UserChatAdapter;
import com.example.doan.AdminActivity.ChatActivity;
import com.example.doan.DatabaseClass.User;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserChatFragment extends Fragment {
    private int userId;
    private RecyclerView recyclerViewUsers;
    private List<User> adminList;
    private List<User> allAdmin = new ArrayList<>(); // Danh sách không đổi để filter

    private UserChatAdapter userAdapter;

    private EditText edtChatSearch;

    public UserChatFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);

        edtChatSearch = view.findViewById(R.id.etChatSearch);

        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        adminList = new ArrayList<>();
        allAdmin = new ArrayList<>();

        userAdapter = new UserChatAdapter(adminList, getContext(), admin -> {
            MarkMessagesAsRead(admin.getId(), userId);
            // 2. Cập nhật trạng thái trong list
            admin.setHasUnread(false);
            userAdapter.notifyDataSetChanged(); // 👈 bắt buộc để đổi màu
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("user_id", admin.getId());
            intent.putExtra("user_name", admin.getName());
            startActivity(intent);
        });

        recyclerViewUsers.setAdapter(userAdapter);

        loadUsers();
        searchUsers();

        return view;
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
        for (User user :allAdmin) {
            if (user.getName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        userAdapter.updateList(filteredList);
    }

    private void loadUsers() {
        SharedPreferences prefs = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userId = prefs.getInt("id", -1);

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        apiService.getAllUsers(userId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allAdmin.clear();
                    adminList.clear();
                    for (User admin : response.body()) {
                        if (admin.getId() == userId || admin.getRole().equals("User")) continue;
                        Log.d("roles", admin.getRole());
                        allAdmin.add(admin);
                    }
                    adminList.addAll(allAdmin);
                    userAdapter.updateList(adminList);

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
}
