package com.example.doan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.UserFragment.UserHistoryFragment;
import com.example.doan.UserFragment.UserShoppingChipsFragment;
import com.example.doan.UserFragment.UserShoppingDrinksFragment;
import com.example.doan.UserFragment.UserShoppingSoupsFragment;

public class UserHistoryPagerAdapter extends FragmentStateAdapter {
    public UserHistoryPagerAdapter(@NonNull UserHistoryFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new UserShoppingChipsFragment();
            case 2:
                return new UserShoppingDrinksFragment();
            default:
                return new UserShoppingSoupsFragment();
        }
    }
}
