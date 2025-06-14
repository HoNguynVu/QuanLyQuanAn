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
    String username;
    String email;
    String phone;
    String dob;
    List<ProfileOption> items;

    public AdminProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_admin, container, false);

        init(view);

        return view;
    }

    public void init(View view)
    {
        txtUsername = view.findViewById(R.id.usernameTextView);
        txtEmail = view.findViewById(R.id.emailTextView);
        ProfileRecyclerView = view.findViewById(R.id.profileRecyclerView);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        email = sharedPreferences.getString("email", null);
        phone = sharedPreferences.getString("phone", null);
        dob = sharedPreferences.getString("dob", null);

        ProfileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        items = new ArrayList<>();
        items.add(new ProfileOption(R.drawable.ic_order, "Đơn hàng"));
        items.add(new ProfileOption(R.drawable.ic_settings, "Cài đặt"));
        items.add(new ProfileOption(R.drawable.ic_logout, "Đăng xuất"));

        setAdapter(sharedPreferences);
        ProfileRecyclerView.setAdapter(adapter);
    }

    public void setAdapter(SharedPreferences sharedPreferences) {
        adapter = new ProfileOptionAdapter(items, option-> {
            switch (option.getTitle()) {
                case "Đăng xuất":
                    Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    AdminProfileFragment.this.getActivity().finish();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    break;
                case "Đơn hàng":
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new AdminOrdersFragment())
                            .addToBackStack(null)
                            .commit();
                    ((AdminHome) requireActivity()).navigateToOrders();
                    break;
                case "Cài đặt":
                    Intent intent2 = new Intent(getContext(), AdminSetting.class);
                    startActivity(intent2);
                    break;
            }
        });
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
