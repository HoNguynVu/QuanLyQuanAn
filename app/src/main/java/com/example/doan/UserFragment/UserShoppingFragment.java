package com.example.doan.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UserShoppingFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSearchView();
        setTabLayoutAndViewPager();
    }

    public void setSearchView() {
        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
                navController.navigate(R.id.searchFragment);
            }
        });
    }

    public void setTabLayoutAndViewPager() {
        tabLayout = binding.menuContainer;
        viewPager2 = binding.viewPager;

        UserShoppingPagerAdapter myViewPagerAdapter = new UserShoppingPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.user_shopping_tablayout_item, null);
            ImageView tabIcon = tabView.findViewById(R.id.tabIcon);
            TextView tabText = tabView.findViewById(R.id.tabText);
            switch (position) {
                case 1:
                    tabText.setText("Khai vị");
                    tabIcon.setImageResource(R.drawable.ic_appetizer);
                    break;
                case 2:
                    tabText.setText("Đồ uống");
                    tabIcon.setImageResource(R.drawable.ic_drink);
                    break;
                case 3:
                    tabText.setText("Tráng miệng");
                    tabIcon.setImageResource(R.drawable.ic_dessert);
                    break;
                default:
                    tabText.setText("Món chính");
                    tabIcon.setImageResource(R.drawable.ic_main_course);
            }

            tab.setCustomView(tabView);
        }).attach();

    }
}