package com.example.doan;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.doan.Adapter.CartAdapter;
import com.example.doan.databinding.ActivityCartBinding;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    List<Item> cartList = List.of(
            new Item("Pizza", "10$", R.drawable.soup_celery),
            new Item("Burger", "10$", R.drawable.soup_dimsum),
            new Item("Hotdog", "10$", R.drawable.kale_soup),
            new Item("Drink", "10$", R.drawable.soup_mushroom)
    );
    CartAdapter adapter;
    private ActivityCartBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new CartAdapter(cartList, this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), LinearLayout.VERTICAL);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);


        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(10));
    }


}