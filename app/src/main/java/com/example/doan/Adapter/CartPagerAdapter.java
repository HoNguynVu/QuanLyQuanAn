package com.example.doan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.Fragment.CartAFragment;
import com.example.doan.Fragment.CartDrinksFragment;
import com.example.doan.Fragment.CartSoupsFragment;
import com.example.doan.Fragment.CartFragment;

public class CartPagerAdapter extends FragmentStateAdapter {
    public CartPagerAdapter(@NonNull CartFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CartSoupsFragment();
            case 1:
                return new CartAFragment();
            case 2:
                return new CartDrinksFragment();
            default:
                return new CartSoupsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
