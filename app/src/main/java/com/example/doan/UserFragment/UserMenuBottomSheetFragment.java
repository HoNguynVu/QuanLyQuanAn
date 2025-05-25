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
import com.example.doan.Adapter.UserCartAdapter;
import com.example.doan.Adapter.UserMenuAdapter;
import com.example.doan.UserDataFetcher;
import com.example.doan.UserItem;
import com.example.doan.R;
import com.example.doan.UserSpaceItemDecoration;
import com.example.doan.databinding.UserFragmentMenuBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import static com.example.doan.UserConstants.GETFOODS_URL;

public class UserMenuBottomSheetFragment extends BottomSheetDialogFragment {
    private UserFragmentMenuBottomSheetBinding binding;
    UserMenuAdapter adapter;
    List<UserItem> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentMenuBottomSheetBinding.inflate(inflater, container, false);

        adapter = new UserMenuAdapter(requireContext(), itemList);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.menuRecyclerView.getContext(), LinearLayout.VERTICAL);
        binding.menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.menuRecyclerView.setAdapter(adapter);
        binding.menuRecyclerView.addItemDecoration(dividerItemDecoration);
        binding.menuRecyclerView.addItemDecoration(new UserSpaceItemDecoration(16));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserDataFetcher.fetchFoods(requireContext(), GETFOODS_URL, new UserDataFetcher.FetchCallBack() {

            @Override
            public void onSuccess(List<UserItem> data) {
                Log.d(TAG, "onSuccess: " + data);
                itemList.clear();
                itemList.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }
}