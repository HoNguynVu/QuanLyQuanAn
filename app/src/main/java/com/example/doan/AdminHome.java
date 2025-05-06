package com.example.doan;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHome extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Load Fragment đầu tiên (Trang chủ hoặc Menu mặc định)
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new AdminHomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if(item.getItemId()==R.id.nav_home){
                toolbar.setTitle("Quản lý quán ăn");
                selectedFragment = new AdminHomeFragment();
            }
            if(item.getItemId()==R.id.nav_orders){
                toolbar.setTitle("Đơn hàng");
                selectedFragment = new AdminOrdersFragment();
            }
            if (item.getItemId()==R.id.nav_menu) {
                toolbar.setTitle("Menu món ăn");
                selectedFragment = new AdminMenuFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}