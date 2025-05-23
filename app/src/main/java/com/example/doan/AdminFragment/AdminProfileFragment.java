package com.example.doan.AdminFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.AdminActivity.AdminSetting;
import com.example.doan.Login.LoginActivity;
import com.example.doan.ProfileUser.MyProfileActivity;
import com.example.doan.ProfileUser.ProfileOption;
import com.example.doan.ProfileUser.ProfileOptionAdapter;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class AdminProfileFragment extends Fragment {

    private TextView txtUsername, txtEmail;
    private RecyclerView ProfileRecyclerView;
    private ProfileOptionAdapter adapter;

    public AdminProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_admin, container, false);
        
        txtUsername = view.findViewById(R.id.usernameTextView);
        txtEmail = view.findViewById(R.id.emailTextView);
        ProfileRecyclerView = view.findViewById(R.id.profileRecyclerView);
        
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);
        String phone = sharedPreferences.getString("phone", null);
        String dob = sharedPreferences.getString("dob", null);
        
        ProfileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<ProfileOption> items = new ArrayList<>();
        items.add(new ProfileOption(R.drawable.ic_person, "My Profile"));
        items.add(new ProfileOption(R.drawable.ic_settings, "Settings"));
        items.add(new ProfileOption(R.drawable.ic_logout, "Log Out"));
        
        adapter = new ProfileOptionAdapter(items, option-> {
            switch (option.getTitle()) {
                case "Log Out":
                    Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    AdminProfileFragment.this.getActivity().finish();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    break;
                case "My Profile":
                    Toast.makeText(getContext(), "Điều hướng đến My Profile", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getContext(), MyProfileActivity.class);
                    intent1.putExtra("username", txtUsername.getText().toString());
                    intent1.putExtra("email", txtEmail.getText().toString());
                    intent1.putExtra("phone", phone);
                    intent1.putExtra("dob", dob);
                    startActivity(intent1);
                    break;
                case "Settings":
                    Toast.makeText(getContext(), "Điều hướng đến Settings", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getContext(), AdminSetting.class);
                    startActivity(intent2);
                    break;
            }
        });
        ProfileRecyclerView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void loadUserInfo() {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Tên người dùng");
        String email = prefs.getString("email", "Email");

        if (username != null && email != null) {
            txtUsername.setText(username);
            txtEmail.setText(email);
        }
        else
        {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

}
