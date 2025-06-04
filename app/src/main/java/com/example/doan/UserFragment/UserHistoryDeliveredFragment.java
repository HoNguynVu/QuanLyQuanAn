package com.example.doan.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan.Adapter.UserHistoryAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.R;
import com.example.doan.databinding.UserHistoryDeliveredFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class UserHistoryDeliveredFragment extends Fragment {

    UserHistoryAdapter adapter;
    private UserHistoryDeliveredFragmentBinding binding;
    List<FoodItem> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserHistoryDeliveredFragmentBinding.inflate(inflater, container, false);
        adapter = new UserHistoryAdapter(requireContext(), itemList);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}