package com.example.doan.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan.Adapter.MyViewPagerAdapter;
import com.example.doan.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CartFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.menu_container);
        viewPager2 = view.findViewById(R.id.view_pager);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.custom_tablayout, null);
            ImageView tabIcon = tabView.findViewById(R.id.tabIcon);
            TextView tabText = tabView.findViewById(R.id.tabText);
            switch (position) {
                case 1:
                    tabText.setText("A");
                    tabIcon.setImageResource(R.drawable.ic_a);
                    break;
                case 2:
                    tabText.setText("B");
                    break;
                default:
                    tabText.setText("Food List");
            }

            tab.setCustomView(tabView);
        }).attach();
    }

}