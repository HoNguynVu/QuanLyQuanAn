package com.example.doan.UserFragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.example.doan.Adapter.UserMenuAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.User.UserDataFetcher;
import com.example.doan.User.UserSpaceItemDecoration;
import com.example.doan.databinding.UserFragmentMenuBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class UserMenuBottomSheetFragment extends BottomSheetDialogFragment {
    private UserFragmentMenuBottomSheetBinding binding;
    UserMenuAdapter adapter;
    List<FoodItem> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentMenuBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView();
        getFoodData();
    }

    public void setRecyclerView() {
        adapter = new UserMenuAdapter(requireContext(), itemList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.menuRecyclerView.getContext(), LinearLayout.VERTICAL);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.menuRecyclerView.setAdapter(adapter);
        binding.menuRecyclerView.addItemDecoration(dividerItemDecoration);
        binding.menuRecyclerView.addItemDecoration(new UserSpaceItemDecoration(16));
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