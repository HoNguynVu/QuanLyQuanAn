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
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.doan.Adapter.UserMenuAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.UserDataFetcher;
import com.example.doan.UserSpaceItemDecoration;
import com.example.doan.databinding.UserFragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class UserSearchFragment extends Fragment {
    private UserFragmentSearchBinding binding;
    private UserMenuAdapter adapter;

    List<FoodItem> itemList = new ArrayList<>();
    List<FoodItem> filteredList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserFragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filteredList = new ArrayList<>();
        adapter = new UserMenuAdapter(requireContext(), filteredList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredMenu(newText);
                return true;
            }
        });

        UserDataFetcher.fetchFoods(requireContext(), GETFOODS_URL, new UserDataFetcher.FetchCallBack() {

            @Override
            public void onSuccess(List<FoodItem> data) {
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

    private void filteredMenu(String newText) {
        filteredList.clear();
        if(newText != null && !newText.isEmpty()) {
            for (FoodItem item : itemList) {
                if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.GONE);
        }

        if(filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }

}