package com.example.doan.UserFragment;

import static android.content.ContentValues.TAG;
import static com.example.doan.UserConstants.GETFOODS_URL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.doan.Adapter.UserMenuAdapter;
import com.example.doan.UserDataFetcher;
import com.example.doan.UserItem;
import com.example.doan.R;
import com.example.doan.UserSpaceItemDecoration;
import com.example.doan.databinding.UserShoppingSoupsFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class UserShoppingSoupsFragment extends Fragment {
    UserMenuAdapter adapter;
    private UserShoppingSoupsFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    List<UserItem> itemList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserShoppingSoupsFragmentBinding.inflate(inflater, container, false);

        adapter = new UserMenuAdapter(requireContext(), itemList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));
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