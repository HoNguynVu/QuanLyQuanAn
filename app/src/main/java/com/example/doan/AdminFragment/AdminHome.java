package com.example.doan.AdminFragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.doan.R;
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
                selectedFragment = new AdminHomeFragment();
            }
            if(item.getItemId()==R.id.nav_orders){
                selectedFragment = new AdminOrdersFragment();
            }
            if (item.getItemId()==R.id.nav_menu) {
                selectedFragment = new AdminMenuFragment();
            }
            if (item.getItemId()==R.id.nav_profile) {
                selectedFragment = new AdminProfileFragment();
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