package com.example.doan.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.doan.Adapter.PopularItemAdapter;
import com.example.doan.R;
import com.example.doan.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<String> titles;
    private List<String> prices;
    private List<Integer> images;
    private PopularItemAdapter adapter;
    private FragmentHomeBinding binding;

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
        images.add(R.drawable.celery_soup);
        images.add(R.drawable.dimsum_soup);
        images.add(R.drawable.kale_soup);
        images.add(R.drawable.mashroom_soup);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        addAllTitles();
        addAllPrices();
        addAllImages();

        adapter = new PopularItemAdapter(requireContext(), titles, prices, images, getActivity());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT);

        binding.imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                String itemMessage = "Selected image " + (position + 1);
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
