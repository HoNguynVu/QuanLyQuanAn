package com.example.doan.Fragment;

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
import com.example.doan.Profile.ChangePasswordActivity;
import com.example.doan.Profile.MyProfileActivity;
import com.example.doan.Profile.ProfileOption;
import com.example.doan.Profile.ProfileOptionAdapter;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private TextView usernameTextView;
    private TextView emailTextView;
    private RecyclerView profileRecyclerView;
    private ProfileOptionAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        // Ánh xạ view
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        profileRecyclerView = view.findViewById(R.id.profileRecyclerView);

        // Lấy thông tin người dùng từ SharedPreferences (nếu có)
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);
        String phone = sharedPreferences.getString("phone", null);
        String dob = sharedPreferences.getString("dob", null);

        // Khởi tạo RecyclerView
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tạo danh sách các tùy chọn cho profile
        List<ProfileOption> items = new ArrayList<>();
        items.add(new ProfileOption(R.drawable.ic_person, "My Profile"));
        items.add(new ProfileOption(R.drawable.ic_orders, "My Orders"));
        items.add(new ProfileOption(R.drawable.ic_password, "Change Password"));
        items.add(new ProfileOption(R.drawable.ic_logout, "Log Out"));

        // Khởi tạo adapter và set cho RecyclerView
        adapter = new ProfileOptionAdapter(items, option -> {
            // Xử lý các sự kiện khi chọn các option
            switch (option.getTitle()) {
                case "Log Out":
                    // Logic đăng xuất
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    ProfileFragment.this.getActivity().finish();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    break;
                case "My Profile":
                    // Điều hướng đến màn hình profile (có thể thêm logic khác)
                    Toast.makeText(getContext(), "Điều hướng đến My Profile", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getContext(), MyProfileActivity.class);
                    intent1.putExtra("username", usernameTextView.getText().toString());
                    intent1.putExtra("email", emailTextView.getText().toString());
                    intent1.putExtra("phone", phone);
                    intent1.putExtra("dob", dob);
                    startActivity(intent1);
                    break;
                case "My Orders":
                    // Điều hướng đến màn hình đơn hàng
                    Toast.makeText(getContext(), "Điều hướng đến My Orders", Toast.LENGTH_SHORT).show();
                    break;
                case "Change Password":
                    // Điều hướng đến màn hình thay đổi mật khẩu
                    Toast.makeText(getContext(), "Điều hướng đến Change Password", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getContext(), ChangePasswordActivity.class);
                    startActivity(intent2);
                    break;
            }
        });

        profileRecyclerView.setAdapter(adapter);


        // Hiển thị thông tin người dùng

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo(); // gọi lại hàm hiển thị tên/email/ảnh... từ SharedPreferences
    }

    private void loadUserInfo() {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Tên người dùng");
        String email = prefs.getString("email", "Email");

        if (username != null && email != null) {
            usernameTextView.setText(username);
            emailTextView.setText(email);
        }
        else
        {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }
}
