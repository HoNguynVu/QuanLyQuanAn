package com.example.doan.UserActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.doan.R;
import com.example.doan.User.UserCartManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_main);

        setBottomNavigation();
        UserCartManager.getInstance().initialize(this, () -> {
            runOnUiThread(() -> {
                // Cập nhật UI giỏ hàng ở đây (RecyclerView chẳng hạn)
            });
        });
    }

    public void setBottomNavigation() {
        NavController navController = NavHostFragment.findNavController(
                (NavHostFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView))
        );
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.searchFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

    }
}