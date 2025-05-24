package com.example.doan.UserFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.doan.Adapter.UserCartAdapter;
import com.example.doan.UserItem;
import com.example.doan.R;
import com.example.doan.UserSpaceItemDecoration;
import com.example.doan.databinding.UserFragmentMenuBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class UserMenuBottomSheetFragment extends BottomSheetDialogFragment {

    private UserFragmentMenuBottomSheetBinding binding;
    List<UserItem> cartList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentMenuBottomSheetBinding.inflate(inflater, container, false);

        UserCartAdapter adapter = new UserCartAdapter(cartList, requireContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.menuRecyclerView.getContext(), LinearLayout.VERTICAL);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.menuRecyclerView.setAdapter(adapter);
        binding.menuRecyclerView.addItemDecoration(dividerItemDecoration);
        binding.menuRecyclerView.addItemDecoration(new UserSpaceItemDecoration(16));
        return binding.getRoot();
    }
}