package com.example.doan.UserFragment;

import static com.example.doan.User.UserConstants.CREATE_ORDER_URL;
import static com.example.doan.User.UserConstants.GET_ORDER_ITEMS_URL;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.example.doan.Adapter.UserHistoryAdapter;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.DatabaseClass.Order;
import com.example.doan.DatabaseClass.OrderItem;
import com.example.doan.R;
import com.example.doan.User.UserDataFetcher;
import com.example.doan.User.UserSpaceItemDecoration;
import com.example.doan.databinding.UserHistoryDeliveredFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class UserHistoryDeliveredFragment extends Fragment {

    UserHistoryAdapter adapter;
    private UserHistoryDeliveredFragmentBinding binding;
    List<Order> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UserHistoryDeliveredFragmentBinding.inflate(inflater, container, false);


        List<OrderItem> items = new ArrayList<>();
        for(Order item : itemList) {
            items.add(item.getItems().get(0));
        }


        adapter = new UserHistoryAdapter(requireContext(), items);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.addItemDecoration(new UserSpaceItemDecoration(16));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDataFetcher.FetchOrderItems(requireContext(), GET_ORDER_ITEMS_URL, new UserDataFetcher.FetchCallBack<Order>() {
            @Override
            public void onSuccess(List<Order> data) {
                itemList.clear();
                itemList.addAll(data);

                List<OrderItem> items = new ArrayList<>();
                for (Order order : data) {
                    if (order.getItems() != null && !order.getItems().isEmpty()) {
                        items.add(order.getItems().get(0));
                    }
                }
                adapter.updateData(items);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}