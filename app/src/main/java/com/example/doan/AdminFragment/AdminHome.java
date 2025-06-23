package com.example.doan.AdminFragment;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.doan.Interface.ToolbarController;
import com.example.doan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHome extends AppCompatActivity implements ToolbarController {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        toolbar = findViewById(R.id.topAppBar);
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
                toolbar.setTitle("Quản lý đơn hàng");
            }
            if (item.getItemId()==R.id.nav_menu) {
                selectedFragment = new AdminMenuFragment();
                toolbar.setTitle("Quản lý Menu");
            }
            if (item.getItemId()==R.id.nav_profile) {
                selectedFragment = new AdminProfileFragment();
                toolbar.setTitle("Thông tin cá nhân");
            }
            if (item.getItemId()==R.id.nav_chat) {
                selectedFragment = new AdminChatFragment();
                toolbar.setTitle("Chat");
            }
            if (item.getItemId()==R.id.nav_chat) {
//                selectedFragment = new AdminChatFragment();
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
    
    public void navigateToOrders(){
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);
    }
    
    // Implementation của ToolbarController interface
    @Override
    public void showToolbar(boolean show) {
        if (toolbar != null) {
            toolbar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
    
    @Override
    public void setToolbarTitle(String title, boolean showBackButton) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            getSupportActionBar().setDisplayShowHomeEnabled(showBackButton);
        }
    }

}