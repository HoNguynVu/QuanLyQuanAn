package com.example.doan.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.doan.Adapter.HomePopularItemAdapter;
import com.example.doan.CartActivity;
import com.example.doan.Item;
import com.example.doan.R;
import com.example.doan.SpaceItemDecoration;
import com.example.doan.databinding.HomeFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    HomePopularItemAdapter adapter;
    private HomeFragmentBinding binding;

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
        binding = HomeFragmentBinding.inflate(inflater, container, false);

        adapter = new HomePopularItemAdapter(requireContext(), itemList);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.viewall.setOnClickListener(v -> {
            MenuBottomSheetFragment bottomSheetDialog = new MenuBottomSheetFragment();
            bottomSheetDialog.show(getParentFragmentManager(), "Test");
        });

        binding.cartFragment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });

        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, new SearchFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        var imageList = new ArrayList<SlideModel>();
        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        var imageSlider = binding.imageSlider;
        imageSlider.setImageList(imageList);
        imageSlider.setImageList(imageList, ScaleTypes.FIT);


        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void doubleClick(int i) {

            }

            @Override
            public void onItemSelected(int position) {
                // xử lý gì đó với item
                String itemMessage = "Selected image " + (position + 1);
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show();
            }
        });

        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(16));
    }
}