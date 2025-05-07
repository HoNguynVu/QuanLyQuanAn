package com.example.doan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.ChildFragment.AFragment;
import com.example.doan.ChildFragment.BFragment;
import com.example.doan.ChildFragment.FoodListFragment;
import com.example.doan.UserFragment.CartFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull CartFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FoodListFragment();
            case 1:
                return new AFragment();
            case 2:
                return new BFragment();
            default:
                return new FoodListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
