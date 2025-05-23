package com.example.doan.UserFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.doan.Adapter.UserHomePopularItemAdapter;
import com.example.doan.UserCartActivity;
import com.example.doan.UserItem;
import com.example.doan.R;
import com.example.doan.UserSpaceItemDecoration;
import com.example.doan.databinding.UserHomeFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {
    UserHomePopularItemAdapter adapter;
    private UserHomeFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    List<UserItem> itemList = List.of(
            new UserItem("Pizza", "10$", R.drawable.soup_celery, "1"),
            new UserItem("Burger", "10$", R.drawable.soup_dimsum, "1"),
            new UserItem("Hotdog", "10$", R.drawable.kale_soup, "1"),
            new UserItem("Drink", "10$", R.drawable.soup_mushroom, "1")
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserHomeFragmentBinding.inflate(inflater, container, false);

        adapter = new UserHomePopularItemAdapter(requireContext(), itemList);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.viewall.setOnClickListener(v -> {
            UserMenuBottomSheetFragment bottomSheetDialog = new UserMenuBottomSheetFragment();
            bottomSheetDialog.show(getParentFragmentManager(), "Test");
        });

        binding.cartFragment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserCartActivity.class);
            startActivity(intent);
        });

        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, new UserSearchFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
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
                String itemMessage = "Bạn đã chọn ảnh " + (position + 1);
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show();
            }
            public void doubleClick(int position) {

            }
        });

        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));
    }
}
