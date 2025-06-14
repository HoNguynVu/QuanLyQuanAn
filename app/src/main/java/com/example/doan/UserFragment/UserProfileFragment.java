package com.example.doan.UserFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Login.LoginActivity;
import com.example.doan.ProfileUser.ChangePasswordActivity;
import com.example.doan.ProfileUser.MyOrdersActivity;
import com.example.doan.ProfileUser.MyProfileActivity;
import com.example.doan.ProfileUser.ProfileOption;
import com.example.doan.ProfileUser.ProfileOptionAdapter;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment {

    private TextView usernameTextView;
    private TextView emailTextView;
    private RecyclerView profileRecyclerView;

    public UserProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        initViews(view);
        loadUserInfo();
        setupRecyclerView();
        return view;
    }

    // Khởi tạo các View trong layout
    private void initViews(View view) {
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        profileRecyclerView = view.findViewById(R.id.profileRecyclerView);
    }

    // Lấy thông tin người dùng từ CurrentUser và hiển thị lên UI
    private void loadUserInfo() {

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String email = prefs.getString("email", "");

        if (username != null && email != null) {
            usernameTextView.setText(username);
            emailTextView.setText(email);
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    // Thiết lập RecyclerView với danh sách các tùy chọn trong profile
    private void setupRecyclerView() {
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProfileOption> options = new ArrayList<>();
        options.add(new ProfileOption(R.drawable.ic_person, "Hồ sơ của tôi"));
        options.add(new ProfileOption(R.drawable.ic_orders, "Lịch sử đặt hàng"));
        options.add(new ProfileOption(R.drawable.ic_password, "Thay đổi mật khẩu"));
        options.add(new ProfileOption(R.drawable.ic_logout, "Đăng xuất"));

        ProfileOptionAdapter adapter = new ProfileOptionAdapter(options, option -> {
            handleProfileOptionClick(option.getTitle());
        });

        profileRecyclerView.setAdapter(adapter);
    }

    // Xử lý các sự kiện click trên từng mục tùy chọn profile
    private void handleProfileOptionClick(String optionTitle) {
        switch (optionTitle) {
            case "Đăng xuất":
                Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                UserProfileFragment.this.getActivity().finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;

            case "Hồ sơ của tôi":
                Intent intentProfile = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intentProfile);
                break;

            case "Thay đổi mật khẩu":
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
                break;

            case "Lịch sử đặt hàng":
                Intent intentOrders = new Intent(getContext(), MyOrdersActivity.class);
                startActivity(intentOrders);
                break;
        }
    }

    // Reload lại thông tin người dùng khi Fragment được hiển thị lại
    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

}
