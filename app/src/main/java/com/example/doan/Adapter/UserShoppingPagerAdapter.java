package com.example.doan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.UserFragment.UserShoppingChipsFragment;
import com.example.doan.UserFragment.UserShoppingDrinksFragment;
import com.example.doan.UserFragment.UserShoppingSoupsFragment;
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
                return new UserShoppingChipsFragment();
            case 2:
                return new UserShoppingDrinksFragment();
            default:
                return new UserShoppingSoupsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
