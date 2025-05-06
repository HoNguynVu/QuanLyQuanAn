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

import com.example.doan.Adapter.HomePopularItemAdapter;
import com.example.doan.R;
import com.example.doan.databinding.CartAFragmentBinding;

import java.util.ArrayList;
import java.util.List;


public class CartAFragment extends Fragment {
    List<String> titles;
    List<String> prices;
    List<Integer> images;
    HomePopularItemAdapter adapter;
    private CartAFragmentBinding binding;

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
        Log.d("onCreateView", "onCreateView: ");
        binding = CartAFragmentBinding.inflate(inflater, container, false);

        addAllTitles();
        addAllPrices();
        addAllImages();
        adapter = new HomePopularItemAdapter(requireContext(), titles, prices, images, getActivity());

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return binding.getRoot();
    }


}