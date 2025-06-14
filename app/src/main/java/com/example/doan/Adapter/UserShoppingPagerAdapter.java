package com.example.doan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.UserFragment.UserShoppingAppetizerFragment;
import com.example.doan.UserFragment.UserShoppingDessertFragment;
import com.example.doan.UserFragment.UserShoppingDrinksFragment;
import com.example.doan.UserFragment.UserShoppingMainCourseFragment;
import com.example.doan.UserFragment.UserShoppingFragment;

public class UserShoppingPagerAdapter extends FragmentStateAdapter {
    public UserShoppingPagerAdapter(@NonNull UserShoppingFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new UserShoppingAppetizerFragment();
            case 2:
                return new UserShoppingDrinksFragment();
            case 3:
                return new UserShoppingDessertFragment();
            default:
                return new UserShoppingMainCourseFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
