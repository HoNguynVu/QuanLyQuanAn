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
import com.example.doan.Item;
import com.example.doan.R;
import com.example.doan.databinding.CartFragmentBinding;

import java.util.List;

public class CartFragment extends Fragment {
    List<Item> cartList = List.of(
            new Item("Pizza", "10$", R.drawable.soup_celery),
            new Item("Burger", "10$", R.drawable.soup_dimsum),
            new Item("Hotdog", "10$", R.drawable.kale_soup),
            new Item("Drink", "10$", R.drawable.soup_mushroom)
    );
    CartAdapter adapter;
    private CartFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CartFragmentBinding.inflate(inflater, container, false);

        adapter = new CartAdapter(cartList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }
}