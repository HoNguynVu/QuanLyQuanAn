package com.example.doan.UserFragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan.Adapter.UserMenuAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.User.UserDataFetcher;
import com.example.doan.User.UserSpaceItemDecoration;
import com.example.doan.databinding.UserShoppingDessertFragmentBinding;

import java.util.ArrayList;
import java.util.List;


public class UserShoppingDessertFragment extends Fragment {
    UserMenuAdapter adapter;
    private UserShoppingDessertFragmentBinding binding;

    List<FoodItem> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserShoppingDessertFragmentBinding.inflate(inflater, container, false);

        adapter = new UserMenuAdapter(requireContext(), itemList);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));

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
        });
    }
}