package com.example.doan.UserFragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;


import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.doan.Adapter.UserMenuAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.Order;
import com.example.doan.User.UserDataFetcher;
import com.example.doan.User.UserSpaceItemDecoration;
import com.example.doan.databinding.UserSearchFragmentBinding;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserSearchFragment extends Fragment {
    private UserSearchFragmentBinding binding;
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
        binding = UserSearchFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView();
        getFoodData();
        //setSearchView();
        setBtnBack();
        view.postDelayed(this::setSearchView, 300);
    }

    public void setBtnBack() {
        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }
    public void setRecyclerView() {
        filteredList = new ArrayList<>();
        adapter = new UserMenuAdapter(requireContext(), filteredList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));
    }

    public void setSearchView() {
        binding.searchView.setIconified(false); // mở rộng ô tìm kiếm

        EditText editText = binding.searchView.findViewById(
                getResources().getIdentifier("search_src_text", "id", requireContext().getPackageName())
        );

        if (editText != null) {
            Log.d("SearchView", "EditText found");

            // Bắt buộc đặt màu nếu bạn dùng background trắng
            editText.setTextColor(Color.BLACK);
            editText.setHintTextColor(Color.GRAY);
            editText.setHint("Bạn muốn đặt gì?");

            // Focus và hiển thị bàn phím sau khi View hoàn tất
            editText.requestFocus();
            editText.postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    boolean result = imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    Log.d("SearchView", "showSoftInput result = " + result);
                }
            }, 300);
        } else {
            Log.e("SearchView", "EditText NOT FOUND");
        }

        // Lắng nghe thay đổi văn bản
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
    }


    public static String removeAccent(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
    }
    private void filteredMenu(String newText) {
        // Lọc theo từ khóa tìm kiếm
        filteredList.clear();
        if(newText != null && !newText.isEmpty()) {
            for (FoodItem item : itemList) {
                String name = removeAccent(item.getName().toLowerCase());
                String text = removeAccent(newText.toLowerCase());
                if (name.contains(text)) {
                    filteredList.add(item);
                }
            }
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.GONE);
        }

        adapter.setFilteredList(filteredList);
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