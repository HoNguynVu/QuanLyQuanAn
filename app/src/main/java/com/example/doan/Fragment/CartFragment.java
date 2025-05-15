package com.example.doan.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.doan.Adapter.CartAdapter;
import com.example.doan.Adapter.HomePopularItemAdapter;
import com.example.doan.R;
import com.example.doan.databinding.CartFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    List<String> titles;
    List<String> prices;
    List<Integer> images;
    CartAdapter adapter;
    private CartFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    private void addAllTitles() {
        titles = new ArrayList<>();
        titles.add("Pizza");
        titles.add("Burger");
        titles.add("Hotdog");
        titles.add("Drink");
    }

    private void addAllPrices() {
        prices = new ArrayList<>();
        prices.add("10$");
        prices.add("10$");
        prices.add("10$");
        prices.add("10$");
    }

    private void addAllImages() {
        images = new ArrayList<>();
        images.add(R.drawable.soup_celery);
        images.add(R.drawable.soup_dimsum);
        images.add(R.drawable.kale_soup);
        images.add(R.drawable.soup_mushroom);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CartFragmentBinding.inflate(inflater, container, false);

        addAllTitles();
        addAllPrices();
        addAllImages();
        adapter = new CartAdapter(titles, prices, images);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }
}