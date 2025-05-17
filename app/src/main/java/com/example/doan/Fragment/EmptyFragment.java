package com.example.doan.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.doan.Adapter.CartAdapter;
import com.example.doan.Item;
import com.example.doan.R;
import com.example.doan.databinding.FragmentEmptyBinding;

import java.util.ArrayList;
import java.util.List;

public class EmptyFragment extends Fragment {
    private FragmentEmptyBinding binding;
    private CartAdapter adapter;

    List<Item> cartList;
    List<Item> filteredList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmptyBinding.inflate(inflater, container, false);

        //adapter = new CartAdapter(cartList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        //setupSearchView();

        return binding.getRoot();
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMenuItems(newText);
                return true;
            }
        });
    }

    private void filterMenuItems(String query) {
        filteredList = new ArrayList<>();

        for(Item item : cartList){
            if(item.getItemName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

/*        if(filteredList.isEmpty()){
            Toast.makeText(requireContext(), "No items found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }*/
    }
}