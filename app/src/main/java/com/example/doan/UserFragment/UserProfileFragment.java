package com.example.doan.UserFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.doan.Login.LoginActivity;
import com.example.doan.ProfileUser.ChangePasswordActivity;
import com.example.doan.ProfileUser.MyOrdersActivity;
import com.example.doan.ProfileUser.MyProfileActivity;
import com.example.doan.R;
import com.example.doan.User.UserCartManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;

public class UserProfileFragment extends Fragment {

    private ShapeableImageView imgAvatar;
    private MaterialTextView txtUsername;
    private MaterialTextView txtEmail;
    private MaterialButton btnEditProfile;
    private MaterialButton btnChangePassword;
    private MaterialButton btnOrderHistory;
    private AppCompatButton btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        initViews(view);
        setupListeners();
        loadUserInfo();
        return view;
    }

    private void initViews(View view) {
        imgAvatar = view.findViewById(R.id.profile_image);
        txtUsername = view.findViewById(R.id.txt_user_name);
        txtEmail = view.findViewById(R.id.txt_user_email);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnOrderHistory = view.findViewById(R.id.btn_order_history);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void setupListeners() {
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyProfileActivity.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyOrdersActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            int userID = sharedPreferences.getInt("id", 0);
            UserCartManager.getInstance().syncCartToServer(requireContext(), userID, () -> {
                UserCartManager.getInstance().cleanCart(requireContext());
                logout();
            });
        });
    }

    private void loadUserInfo() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String email = prefs.getString("email", "");

        txtUsername.setText(username);
        txtEmail.setText(email);
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

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }
}
