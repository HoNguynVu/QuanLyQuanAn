package com.example.doan.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan.Adapter.HomePopularItemAdapter;
import com.example.doan.Item;
import com.example.doan.R;
import com.example.doan.databinding.ShoppingDrinksFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class ShoppingDrinksFragment extends Fragment {
    List<String> titles;
    List<String> prices;
    List<Integer> images;
    HomePopularItemAdapter adapter;
    private ShoppingDrinksFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    List<Item> itemList = List.of(
            new Item("Pizza", "10$", R.drawable.soup_celery),
            new Item("Burger", "10$", R.drawable.soup_dimsum),
            new Item("Hotdog", "10$", R.drawable.kale_soup),
            new Item("Drink", "10$", R.drawable.soup_mushroom)
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("onCreateView", "onCreateView: ");
        binding = ShoppingDrinksFragmentBinding.inflate(inflater, container, false);

        adapter = new HomePopularItemAdapter(requireContext(), itemList);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return binding.getRoot();
    }
}