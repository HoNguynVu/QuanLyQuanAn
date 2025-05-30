package com.example.doan.UserFragment;

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

import com.example.doan.Adapter.UserMenuAdapter;
import com.example.doan.UserItem;
import com.example.doan.R;
import com.example.doan.UserSpaceItemDecoration;
import com.example.doan.databinding.UserFragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class UserSearchFragment extends Fragment {
    private UserFragmentSearchBinding binding;
    private UserMenuAdapter adapter;

    List<UserItem> cartList = List.of(
            new UserItem("Pizza", "10$", R.drawable.soup_celery, "1"),
            new UserItem("Burger", "10$", R.drawable.soup_dimsum, "1"),
            new UserItem("Hotdog", "10$", R.drawable.kale_soup, "1"),
            new UserItem("Drink", "10$", R.drawable.soup_mushroom, "1")
    );
    List<UserItem> filteredList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filteredList = new ArrayList<>();
        adapter = new UserMenuAdapter(filteredList, requireContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));

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
            for (UserItem item : cartList) {
                if (item.getItemName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.GONE);
        }

        if(filteredList.isEmpty()) {

        } else {
            adapter.setFilteredList(filteredList);
        }
    }

}