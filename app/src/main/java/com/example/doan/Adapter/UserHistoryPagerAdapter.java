package com.example.doan.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.UserFragment.UserHistoryCancelledFragment;
import com.example.doan.UserFragment.UserHistoryDeliveredFragment;
import com.example.doan.UserFragment.UserHistoryFragment;
import com.example.doan.UserFragment.UserHistoryWaitingFragment;

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
                return new UserHistoryWaitingFragment();
            case 2:
                return new UserHistoryDeliveredFragment();
            default:
                return new UserHistoryCancelledFragment();
        }
    }
}
