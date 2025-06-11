package com.example.doan.UserFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.NotificationAdapter;
import com.example.doan.DatabaseClass.Notification;
import com.example.doan.DatabaseClass.CurrentUser;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_Notification_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        initRecyclerView(view);
        loadNotifications();
        return view;
    }

    // Khởi tạo RecyclerView và adapter
    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(getContext(), notificationList);
        recyclerView.setAdapter(adapter);
    }

    // Gửi request lấy danh sách thông báo của user hiện tại
    private void loadNotifications() {
        int userId = CurrentUser.getInstance().getId();
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        apiService.getNotifications(userId).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateNotificationList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.d("NotificationFragment", "Load lỗi thông báo: " + t.getMessage());
            }
        });
    }

    // Cập nhật danh sách thông báo và thông báo adapter refresh
    private void updateNotificationList(List<Notification> notifications) {
        notificationList.clear();
        notificationList.addAll(notifications);
        adapter.notifyDataSetChanged();
    }
}
