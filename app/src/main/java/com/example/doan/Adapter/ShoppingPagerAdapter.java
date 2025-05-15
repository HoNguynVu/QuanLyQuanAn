package com.example.doan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.Fragment.ShoppingAFragment;
import com.example.doan.Fragment.ShoppingDrinksFragment;
import com.example.doan.Fragment.ShoppingSoupsFragment;
import com.example.doan.Fragment.ShoppingFragment;

public class ShoppingPagerAdapter extends FragmentStateAdapter {
    public ShoppingPagerAdapter(@NonNull ShoppingFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ShoppingAFragment();
            case 2:
                return new ShoppingDrinksFragment();
            default:
                return new ShoppingSoupsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
