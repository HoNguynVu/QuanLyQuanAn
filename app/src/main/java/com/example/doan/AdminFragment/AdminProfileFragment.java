package com.example.doan.AdminFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.doan.AdminActivity.AdminSetting;
import com.example.doan.Login.LoginActivity;
import com.example.doan.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.imageview.ShapeableImageView;

public class AdminProfileFragment extends Fragment {

    private ShapeableImageView imgAvatar;
    private MaterialTextView txtUserName;
    private MaterialTextView txtUserEmail;
    private MaterialButton btnSetting;
    private MaterialButton btnOrder;
    private AppCompatButton btnLogout;
    public AdminProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_admin, container, false);
        initViews(view);
        loadUserInfo();
        setupListeners();
        return view;
    }

    private void initViews(View view) {
        txtUserName = view.findViewById(R.id.txt_admin_name);
        txtUserEmail = view.findViewById(R.id.txt_admin_email);
        imgAvatar = view.findViewById(R.id.profile_image);

        btnSetting = view.findViewById(R.id.btn_setting);
        btnOrder = view.findViewById(R.id.btn_order);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void loadUserInfo() {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Tên người dùng");
        String email = prefs.getString("email", "Email");

        txtUserName.setText(username);
        txtUserEmail.setText(email);
    }

    private void setupListeners() {
        btnSetting.setOnClickListener(v -> {
            Intent intent2 = new Intent(getContext(), AdminSetting.class);
            startActivity(intent2);
        });

        btnOrder.setOnClickListener(v -> {
            // Chuyển đến Fragment đơn hàng
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new AdminOrdersFragment())
                    .addToBackStack(null)
                    .commit();
            ((AdminHome) requireActivity()).navigateToOrders();

        });

        btnLogout.setOnClickListener(v -> logout());
    }
    private void logout() {
        SharedPreferences.Editor editor = requireActivity()
                .getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
