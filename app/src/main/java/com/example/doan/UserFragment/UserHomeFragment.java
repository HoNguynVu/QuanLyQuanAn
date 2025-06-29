package com.example.doan.UserFragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.doan.Adapter.UserHomePopularItemAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.UserActivity.UserCartActivity;
import com.example.doan.User.UserDataFetcher;
import com.example.doan.R;
import com.example.doan.User.UserSpaceItemDecoration;
import com.example.doan.databinding.UserHomeFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {

    UserHomePopularItemAdapter adapter;
    private UserHomeFragmentBinding binding;
    List<FoodItem> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserHomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBtnCartFragment();
        setViewAll();
        setRecyclerView();
        setSearchView();
        setImageSlider();
        getFoodData();
    }

    public void setSearchView() {
        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
                navController.navigate(R.id.searchFragment);
            }
        });
    }

    public void setRecyclerView() {
        adapter = new UserHomePopularItemAdapter(requireContext(), itemList);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void setViewAll() {
        binding.viewall.setOnClickListener(v -> {
            UserMenuBottomSheetFragment bottomSheetDialog = new UserMenuBottomSheetFragment();
            bottomSheetDialog.show(getParentFragmentManager(), "Test");
        });
    }

    public void setBtnCartFragment() {
        binding.cartFragment.setOnClickListener(v -> {
            binding.cartFragment.setEnabled(false);
            Intent intent = new Intent(getActivity(), UserCartActivity.class);
            startActivity(intent);
            // Bật lại sau 500ms
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.cartFragment.setEnabled(true);
            }, 500);
        });
    }

    public void setImageSlider() {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT);
        binding.imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                if(position==1)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL); // Mở giao diện quay số
                    intent.setData(Uri.parse("tel:0335492367"));    // Số điện thoại cần dán vào
                    startActivity(intent);
                }
            }
            public void doubleClick(int position) {

            }
        });
    }

    public void getFoodData() {
        UserDataFetcher.fetchFoods(new UserDataFetcher.FetchCallBack<FoodItem>() {

            @Override
            public void onSuccess(List<FoodItem> data) {
                Log.d(TAG, "onSuccess: " + data);
                itemList.clear();
                itemList.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.d("Lỗi Retrofit: ", message);
            }
        }, "all");
    }
}
