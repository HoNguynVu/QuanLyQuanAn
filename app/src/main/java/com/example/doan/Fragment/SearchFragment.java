package com.example.doan.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.doan.Adapter.MenuAdapter;
import com.example.doan.Item;
import com.example.doan.R;
import com.example.doan.SpaceItemDecoration;
import com.example.doan.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private MenuAdapter adapter;

    List<Item> cartList = List.of(
            new Item("Pizza", "10$", R.drawable.soup_celery, "1"),
            new Item("Burger", "10$", R.drawable.soup_dimsum, "1"),
            new Item("Hotdog", "10$", R.drawable.kale_soup, "1"),
            new Item("Drink", "10$", R.drawable.soup_mushroom, "1")
    );
    List<Item> filteredList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filteredList = new ArrayList<>();
        adapter = new MenuAdapter(filteredList, requireContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(16));

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredMenu(newText);
                return true;
            }
        });
    }

    private void filteredMenu(String newText) {
        filteredList.clear();
        if(newText != null && !newText.isEmpty()) {
            for (Item item : cartList) {
                if (item.getItemName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.GONE);
        }

        if(filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }

}