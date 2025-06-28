package com.example.doan.AdminFragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.doan.AdminActivity.AdminOrderDetailActivity;
import com.example.doan.DatabaseClass.Order;
import com.example.doan.Adapter.OrderAdapter;
import com.example.doan.Interface.ToolbarController;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersFragment extends Fragment {

    private ListView orderListView;
    private List<Order> orderList;
    private List<Order> filteredOrderList;
    private OrderAdapter orderAdapter;
    private EditText editTextSearch;
    private TextView tvNoResults;
    private ImageButton btnFilter;
    private CardView filterCard;
    private RadioGroup statusRadioGroup;
    private RadioGroup sortRadioGroup;
    private Button btnApplyFilter;

    private String selectedStatus = "all";
    private String selectedSort = "newest";

    private ToolbarController toolbarController;

    public AdminOrdersFragment() {
        super(R.layout.activity_admin_orders);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarController) {
            toolbarController = (ToolbarController) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Ẩn toolbar cho AdminOrdersFragment
        if (toolbarController != null) {
            toolbarController.showToolbar(false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        setupSearchFunctionality();
        setupFilterFunctionality();
        loadOrdersFromServer();
    }



    public void init(View view) {
        orderListView = view.findViewById(R.id.orderListView);
        tvNoResults = view.findViewById(R.id.tvNoResults);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        btnFilter = view.findViewById(R.id.btnFilter);
        filterCard = view.findViewById(R.id.filterCard);
        statusRadioGroup = view.findViewById(R.id.statusRadioGroup);
        sortRadioGroup = view.findViewById(R.id.sortRadioGroup);
        btnApplyFilter = view.findViewById(R.id.btnApplyFilter);
        orderList = new ArrayList<>();
        filteredOrderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(), filteredOrderList);
        orderListView.setAdapter(orderAdapter);
        setupOrderListClickListener();
    }

    private void setupOrderListClickListener() {
        orderListView.setOnItemClickListener((parent, view, position, id) -> {

            Toast.makeText(getContext(), "Mã đơn hàng: " + filteredOrderList.get(position).getOrderId(), Toast.LENGTH_SHORT).show();
            if (position < filteredOrderList.size()) {
                Order selectedOrder = filteredOrderList.get(position);
                Intent intent = new Intent(getContext(), AdminOrderDetailActivity.class);

                intent.putExtra("order_id", selectedOrder.getOrderId());
                intent.putExtra("status", selectedOrder.getStatus());
                intent.putExtra("created_at", selectedOrder.getCreatedAt());
                intent.putExtra("final_amount", selectedOrder.getFinalAmount());
                intent.putExtra("customer_name", selectedOrder.getCustomerName());
                intent.putExtra("user_id", selectedOrder.getUser_id());
                intent.putExtra("address", selectedOrder.getAddress());
                intent.putExtra("phone", selectedOrder.getPhone());
                intent.putExtra("discount_code", selectedOrder.getDiscountCode());
                startActivity(intent);
            }
        });
    }

    private void setupSearchFunctionality() {
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterOrders(editTextSearch.getText().toString());
                return true;
            }
            return false;
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterOrders(s.toString());
            }
        });

        editTextSearch.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // vị trí của drawableEnd
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (editTextSearch.getCompoundDrawables()[DRAWABLE_END] != null) {
                    int leftEdgeOfRightDrawable = editTextSearch.getRight()
                            - editTextSearch.getCompoundDrawables()[DRAWABLE_END].getBounds().width()
                            - editTextSearch.getPaddingEnd();

                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        editTextSearch.setText(""); // xoá nội dung
                        return true;
                    }
                }
            }
            return false;
        });

    }

    private void setupFilterFunctionality() {
        btnFilter.setOnClickListener(v -> {
            filterCard.setVisibility(filterCard.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        btnApplyFilter.setOnClickListener(v -> {
            int statusId = statusRadioGroup.getCheckedRadioButtonId();
            if (statusId == R.id.radioAll) selectedStatus = "all";
            else if (statusId == R.id.radioReceived) selectedStatus = "Đã tiếp nhận";
            else if (statusId == R.id.radioDelivering) selectedStatus = "Đang giao";
            else if (statusId == R.id.radioDelivered) selectedStatus = "Đã giao";
            else if (statusId == R.id.radioCancelled) selectedStatus = "Hủy";
            else if (statusId == R.id.radioWaiting) selectedStatus = "Đang chờ";

            int sortId = sortRadioGroup.getCheckedRadioButtonId();
            if (sortId == R.id.radioNewest) selectedSort = "newest";
            else if (sortId == R.id.radioOldest) selectedSort = "oldest";
            else if (sortId == R.id.radioPriceHigh) selectedSort = "price_high";
            else if (sortId == R.id.radioPriceLow) selectedSort = "price_low";

            filterCard.setVisibility(View.GONE);
            applyFiltersAndSort();
        });
    }

    private void filterOrders(String query) {
        filteredOrderList.clear();
        String searchQuery = unaccent(query.toLowerCase().trim());

        List<Order> searchResults = new ArrayList<>();
        if (searchQuery.isEmpty()) {
            searchResults.addAll(orderList);
        } else {
            for (Order order : orderList) {
                if (orderMatchesSearchCriteria(order, searchQuery)) {
                    searchResults.add(order);
                }
            }
        }

        for (Order order : searchResults) {
            if ("all".equals(selectedStatus) || selectedStatus.equals(order.getStatus())) {
                filteredOrderList.add(order);
            }
        }

        sortFilteredResults();
        orderAdapter.notifyDataSetChanged();
        updateNoResultsView();
    }

    private boolean orderMatchesSearchCriteria(Order order, String searchQuery) {
        if (unaccent(order.getCustomerName()).contains(searchQuery)) return true;
        if (order.getOrderId() != null && order.getOrderId().toLowerCase().contains(searchQuery)) return true;
        if (order.getStatus() != null && unaccent(order.getStatus()).contains(searchQuery)) return true;
        if (order.getCreatedAt() != null && order.getCreatedAt().toLowerCase().contains(searchQuery)) return true;
        String amountString = String.format("%.0f", order.getFinalAmount());
        if (amountString.contains(searchQuery)) return true;

        if (order.getCreatedAt() != null && order.getCreatedAt().length() >= 10) {
            String datePart = order.getCreatedAt().substring(0, 10);
            String[] parts = datePart.split("-");
            if (parts.length == 3) {
                String ddMM = parts[2] + "/" + parts[1];
                String MMdd = parts[1] + "/" + parts[2];
                if (ddMM.contains(searchQuery) || MMdd.contains(searchQuery)) return true;
            }
        }

        return false;
    }

    private void applyFiltersAndSort() {
        filterOrders(editTextSearch.getText().toString());
    }

    private void sortFilteredResults() {
        if (filteredOrderList.size() <= 1) return;

        switch (selectedSort) {
            case "newest":
                filteredOrderList.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
                break;
            case "oldest":
                filteredOrderList.sort((o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
                break;
            case "price_high":
                filteredOrderList.sort((o1, o2) -> Double.compare(o2.getFinalAmount(), o1.getFinalAmount()));
                break;
            case "price_low":
                filteredOrderList.sort((o1, o2) -> Double.compare(o1.getFinalAmount(), o2.getFinalAmount()));
                break;
        }
    }

    private void updateNoResultsView() {
        if (filteredOrderList.isEmpty()) {
            orderListView.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
            tvNoResults.setText("Không tìm thấy đơn hàng nào.");
        } else {
            orderListView.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
        }
    }

    private void loadOrdersFromServer() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Order>> call = apiService.getOrders();

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (!isAdded() || getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    filteredOrderList.clear();
                    filteredOrderList.addAll(orderList);
                    orderAdapter.notifyDataSetChanged();
                    updateNoResultsView();
                } else {
                    Toast.makeText(getContext(), "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String unaccent(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("đ", "d")
                .replace("Đ", "D")
                .toLowerCase();
    }

}
