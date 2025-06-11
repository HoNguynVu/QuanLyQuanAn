package com.example.doan.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan.Adapter.UserShoppingPagerAdapter;
import com.example.doan.R;
import com.example.doan.databinding.UserShoppingFragmentBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserShoppingFragment extends Fragment {
    private UserShoppingFragmentBinding binding;
    UserShoppingPagerAdapter adapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UserShoppingFragmentBinding.inflate(inflater, container, false);
        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, new UserSearchFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.menu_container);
        viewPager2 = view.findViewById(R.id.view_pager);

        UserShoppingPagerAdapter myViewPagerAdapter = new UserShoppingPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.user_shopping_tablayout_item, null);
            ImageView tabIcon = tabView.findViewById(R.id.tabIcon);
            TextView tabText = tabView.findViewById(R.id.tabText);
            switch (position) {
                case 1:
                    tabText.setText("Appetizer");
                    tabIcon.setImageResource(R.drawable.ic_chips);
                    break;
                case 2:
                    tabText.setText("Drink");
                    tabIcon.setImageResource(R.drawable.ic_tea_cup);
                    break;
                case 3:
                    tabText.setText("Dessert");
                    tabIcon.setImageResource(R.drawable.ic_tea_cup);
                    break;
                default:
                    tabText.setText("Main Course");
                    tabIcon.setImageResource(R.drawable.ic_soup);
            }

            tab.setCustomView(tabView);
        }).attach();
    }

}