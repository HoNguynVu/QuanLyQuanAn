package com.example.doan.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan.Adapter.CartAdapter;
import com.example.doan.Item;
import com.example.doan.R;
import com.example.doan.databinding.FragmentMenuBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    private FragmentMenuBottomSheetBinding binding;
    List<Item> cartList = List.of(
            new Item("Pizza", "10$", R.drawable.soup_celery),
            new Item("Burger", "10$", R.drawable.soup_dimsum),
            new Item("Hotdog", "10$", R.drawable.kale_soup),
            new Item("Drink", "10$", R.drawable.soup_mushroom)
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false);

        CartAdapter adapter = new CartAdapter(cartList, requireContext());
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.menuRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}