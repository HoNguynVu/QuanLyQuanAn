package com.example.doan.UserFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_EDIT_PROFILE = 1001;

    private TextView usernameTextView;
    private TextView emailTextView;
    private RecyclerView profileRecyclerView;
    private SharedPreferences sharedPreferences;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        initViews(view);
        loadUserInfo();
        setupRecyclerView();
        return view;
    }

    private void initViews(View view) {
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        profileRecyclerView = view.findViewById(R.id.profileRecyclerView);
        sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    private void loadUserInfo() {
        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);

        if (username != null && email != null) {
            usernameTextView.setText(username);
            emailTextView.setText(email);
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProfileOption> options = new ArrayList<>();
        options.add(new ProfileOption(R.drawable.ic_person, "My Profile"));
        options.add(new ProfileOption(R.drawable.ic_orders, "My Orders"));
        options.add(new ProfileOption(R.drawable.ic_password, "Change Password"));
        options.add(new ProfileOption(R.drawable.ic_logout, "Log Out"));

        ProfileOptionAdapter adapter = new ProfileOptionAdapter(options, option -> {
            switch (option.getTitle()) {
                case "Log Out":
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                    break;

                case "My Profile":
                    Intent intentProfile = new Intent(getContext(), MyProfileActivity.class);
                    intentProfile.putExtra("username", sharedPreferences.getString("username", ""));
                    intentProfile.putExtra("email", sharedPreferences.getString("email", ""));
                    intentProfile.putExtra("phone", sharedPreferences.getString("phone", ""));
                    intentProfile.putExtra("dob", sharedPreferences.getString("dob", ""));
                    editProfileLauncher.launch(intentProfile);
                    break;

                case "Change Password":
                    startActivity(new Intent(getContext(), ChangePasswordActivity.class));
                    break;

                case "My Orders":
                    Toast.makeText(getContext(), "Điều hướng đến My Orders", Toast.LENGTH_SHORT).show();
                    Intent intentOrders = new Intent(getContext(), MyOrdersActivity.class);
                    startActivity(intentOrders);
                    break;
            }
        });

        profileRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();  // luôn reload để đảm bảo hiển thị đúng SharedPreferences
    }

    private ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadUserInfo();
                }
            }
    );
}
