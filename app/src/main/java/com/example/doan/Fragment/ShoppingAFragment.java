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

import com.example.doan.Adapter.MenuAdapter;
import com.example.doan.Item;
import com.example.doan.R;
import com.example.doan.SpaceItemDecoration;
import com.example.doan.databinding.ShoppingAFragmentBinding;

import java.util.List;


public class ShoppingAFragment extends Fragment {
    MenuAdapter adapter;
    private ShoppingAFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    List<Item> itemList = List.of(
            new Item("Pizza", "10$", R.drawable.soup_celery, "1"),
            new Item("Burger", "10$", R.drawable.soup_dimsum, "1"),
            new Item("Hotdog", "10$", R.drawable.kale_soup, "1"),
            new Item("Drink", "10$", R.drawable.soup_mushroom, "1")
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ShoppingAFragmentBinding.inflate(inflater, container, false);

        adapter = new MenuAdapter(itemList, requireContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(16));
        return binding.getRoot();
    }


}