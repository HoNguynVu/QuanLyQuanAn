package com.example.doan.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.doan.Adapter.ShoppingPagerAdapter;
import com.example.doan.R;
import com.example.doan.databinding.ShoppingFragmentBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {
    private ShoppingFragmentBinding binding;
    ShoppingPagerAdapter adapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ShoppingFragmentBinding.inflate(inflater, container, false);
        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Log.d(TAG, "vao duoc");
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, new EmptyFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else {
                Log.d(TAG, "ko vao duoc");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.menu_container);
        viewPager2 = view.findViewById(R.id.view_pager);

        ShoppingPagerAdapter myViewPagerAdapter = new ShoppingPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_tablayout, null);
            ImageView tabIcon = tabView.findViewById(R.id.tabIcon);
            TextView tabText = tabView.findViewById(R.id.tabText);
            switch (position) {
                case 1:
                    tabText.setText("A");
                    tabIcon.setImageResource(R.drawable.ic_a);
                    break;
                case 2:
                    tabText.setText("Drinks");
                    tabIcon.setImageResource(R.drawable.ic_b);
                    break;
                default:
                    tabText.setText("Soups");
                    tabIcon.setImageResource(R.drawable.ic_soups);
            }

            tab.setCustomView(tabView);
        }).attach();
    }

}