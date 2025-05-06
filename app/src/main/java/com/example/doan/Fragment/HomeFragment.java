package com.example.doan.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.doan.Adapter.HomePopularItemAdapter;
import com.example.doan.R;
import com.example.doan.databinding.HomeFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    List<String> titles;
    List<String> prices;
    List<Integer> images;
    HomePopularItemAdapter adapter;
    private HomeFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    private void addAllTitles() {
        titles = new ArrayList<>();
        titles.add("Celery Soup");
        titles.add("Tomato & Potato Soup");
        titles.add("Dimsum Soup");
        titles.add("Mushroom Soup");
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
        images.add(R.drawable.soup_tomato_potato);
        images.add(R.drawable.soup_dimsum);
        images.add(R.drawable.soup_mushroom);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);

        addAllTitles();
        addAllPrices();
        addAllImages();
        adapter = new HomePopularItemAdapter(requireContext(), titles, prices, images, getActivity());

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
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

    }
}