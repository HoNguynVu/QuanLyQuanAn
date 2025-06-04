package com.example.doan.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan.Adapter.UserHistoryPagerAdapter;
import com.example.doan.R;
import com.example.doan.databinding.UserHistoryFragmentBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import org.w3c.dom.Text;

public class UserHistoryFragment extends Fragment {
    private UserHistoryFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserHistoryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserHistoryPagerAdapter myViewPagerAdapter = new UserHistoryPagerAdapter(this);
        binding.viewPager.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.user_history_tablayout_item, null);

            TextView tabText = tabView.findViewById(R.id.tabLayout_item);

            switch (position) {
                case 1:
                    tabText.setText("Đang giao");
                    break;
                case 2:
                    tabText.setText("Đã giao");
                    break;
                default:
                    tabText.setText("Đã hủy");
            }

            tab.setCustomView(tabView);
        }).attach();
    }
}