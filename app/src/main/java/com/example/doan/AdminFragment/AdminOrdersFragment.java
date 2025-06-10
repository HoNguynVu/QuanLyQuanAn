package com.example.doan.AdminFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.doan.DatabaseClass.Order;
import com.example.doan.Adapter.OrderAdapter;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

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
    
    // Thêm các thành phần cho bộ lọc nâng cao
    private ImageButton btnFilter;
    private CardView filterCard;
    private RadioGroup statusRadioGroup;
    private RadioGroup sortRadioGroup;
    private Button btnApplyFilter;
    
    // Các trạng thái lọc và sắp xếp
    private String selectedStatus = "all";
    private String selectedSort = "newest";

    public AdminOrdersFragment() {
        super(R.layout.activity_admin_orders);
    }    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        
        setupSearchFunctionality();
        setupFilterFunctionality();
        loadOrdersFromServer();
    }

    public void init(View view)
    {
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
        
        // Thiết lập click listener cho ListView
        setupOrderListClickListener();
    }
    
    private void setupOrderListClickListener() {
        orderListView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < filteredOrderList.size()) {
                Order selectedOrder = filteredOrderList.get(position);
                  // Tạo Intent để chuyển đến AdminOrderDetailActivity
                Intent intent = new Intent(getContext(), com.example.doan.AdminActivity.AdminOrderDetailActivity.class);
                intent.putExtra("order_id", selectedOrder.getOrderId());
                intent.putExtra("status", selectedOrder.getStatus());
                intent.putExtra("created_at", selectedOrder.getCreatedAt());
                intent.putExtra("final_amount", selectedOrder.getFinalAmount());
                intent.putExtra("customer_name", selectedOrder.getCustomerName());
                intent.putExtra("user_id", selectedOrder.getUser_id());
                
                startActivity(intent);
            }
        });
    }

    private void setupFilterFunctionality() {
        // Xử lý hiển thị/ẩn bộ lọc
        btnFilter.setOnClickListener(v -> {
            if (filterCard.getVisibility() == View.VISIBLE) {
                filterCard.setVisibility(View.GONE);
            } else {
                filterCard.setVisibility(View.VISIBLE);
            }
        });
        
        // Xử lý khi nhấn nút áp dụng bộ lọc
        btnApplyFilter.setOnClickListener(v -> {
            // Lấy trạng thái được chọn
            int statusId = statusRadioGroup.getCheckedRadioButtonId();
            if (statusId == R.id.radioAll) {
                selectedStatus = "all";
            } else if (statusId == R.id.radioPreparing) {
                selectedStatus = "Đang chuẩn bị";
            } else if (statusId == R.id.radioDelivered) {
                selectedStatus = "Đã giao";
            } else if (statusId == R.id.radioCancelled) {
                selectedStatus = "Hủy";
            }
            
            // Lấy kiểu sắp xếp được chọn
            int sortId = sortRadioGroup.getCheckedRadioButtonId();
            if (sortId == R.id.radioNewest) {
                selectedSort = "newest";
            } else if (sortId == R.id.radioOldest) {
                selectedSort = "oldest";
            } else if (sortId == R.id.radioPriceHigh) {
                selectedSort = "price_high";
            } else if (sortId == R.id.radioPriceLow) {
                selectedSort = "price_low";
            }
            
            // Ẩn bộ lọc
            filterCard.setVisibility(View.GONE);
            
            // Lọc và cập nhật danh sách đơn hàng
            applyFiltersAndSort();
        });
    }
    
    private void setupSearchFunctionality() {
        // Xử lý khi nhấn nút tìm kiếm trên bàn phím
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterOrders(editTextSearch.getText().toString());
                return true;
            }
            return false;
        });
        
        // Xử lý khi thay đổi text trong ô tìm kiếm
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterOrders(s.toString());
            }
        });
    }    private void filterOrders(String query) {
        filteredOrderList.clear();
        
        // Lọc theo từ khóa tìm kiếm
        List<Order> searchResults = new ArrayList<>();
        if (query.isEmpty()) {
            searchResults.addAll(orderList);
        } else {
            String searchQuery = query.toLowerCase().trim();
            for (Order order : orderList) {
                // Kiểm tra xem đơn hàng có khớp với tiêu chí tìm kiếm không
                if (orderMatchesSearchCriteria(order, searchQuery)) {
                    searchResults.add(order);
                }
            }
        }
        
        // Áp dụng bộ lọc trạng thái cho kết quả tìm kiếm
        for (Order order : searchResults) {
            if ("all".equals(selectedStatus) || selectedStatus.equals(order.getStatus())) {
                filteredOrderList.add(order);
            }
        }
        
        // Sắp xếp kết quả theo tùy chọn đã chọn
        sortFilteredResults();
        
        // Cập nhật UI
        orderAdapter.notifyDataSetChanged();
        updateNoResultsView();
    }
    
    private void applyFiltersAndSort() {
        // Gọi lại filterOrders với từ khóa hiện tại
        filterOrders(editTextSearch.getText().toString());
    }
    
    private boolean orderMatchesSearchCriteria(Order order, String searchQuery) {
        // Tìm kiếm theo ID đơn hàng
        if (order.getOrderId() != null && order.getOrderId().toLowerCase().contains(searchQuery)) {
            return true;
        }
        
        // Tìm kiếm theo trạng thái (Đang chuẩn bị, Đã giao, Hủy)
        if (order.getStatus() != null && order.getStatus().toLowerCase().contains(searchQuery)) {
            return true;
        }
        
        // Tìm kiếm theo thời gian đặt hàng
        if (order.getCreatedAt() != null && order.getCreatedAt().toLowerCase().contains(searchQuery)) {
            return true;
        }
        
        // Tìm kiếm theo số tiền 
        // Chuyển đổi số tiền thành chuỗi để tìm kiếm
        String amountString = String.valueOf(order.getFinalAmount());
        if (amountString.contains(searchQuery)) {
            return true;
        }

        // Tìm kiếm theo ngày tháng ngắn gọn (ví dụ: tìm "05/22" sẽ tìm được các đơn hàng vào ngày 22/05)
        if (order.getCreatedAt() != null) {
            String dateTime = order.getCreatedAt();
            // Trích xuất chỉ phần ngày tháng từ chuỗi datetime
            if (dateTime.length() > 10) { // giả sử định dạng là "yyyy-MM-dd HH:mm:ss"
                String datePart = dateTime.substring(0, 10);
                if (datePart.replace("-", "/").contains(searchQuery)) {
                    return true;
                }
                
                // Thử với định dạng MM/dd
                try {
                    // Giả sử định dạng là yyyy-MM-dd...
                    String year = datePart.substring(0, 4);
                    String month = datePart.substring(5, 7);
                    String day = datePart.substring(8, 10);
                    
                    // Tạo các định dạng ngày khác nhau để tìm kiếm
                    if ((month + "/" + day).contains(searchQuery) || 
                        (day + "/" + month).contains(searchQuery)) {
                        return true;
                    }
                } catch (Exception e) {
                    // Bỏ qua lỗi parsing ngày tháng
                }
            }
        }
        
        return false;
    }
      private void sortFilteredResults() {
        if (filteredOrderList.size() <= 1) return;
        
        switch (selectedSort) {
            case "newest":
                // Sắp xếp theo thời gian mới nhất
                filteredOrderList.sort((order1, order2) -> {
                    if (order1.getCreatedAt() == null || order2.getCreatedAt() == null) {
                        return 0;
                    }
                    // Sắp xếp giảm dần (mới nhất lên đầu)
                    return order2.getCreatedAt().compareTo(order1.getCreatedAt());
                });
                break;
                
            case "oldest":
                // Sắp xếp theo thời gian cũ nhất
                filteredOrderList.sort((order1, order2) -> {
                    if (order1.getCreatedAt() == null || order2.getCreatedAt() == null) {
                        return 0;
                    }
                    // Sắp xếp tăng dần (cũ nhất lên đầu)
                    return order1.getCreatedAt().compareTo(order2.getCreatedAt());
                });
                break;
                
            case "price_high":
                // Sắp xếp theo giá từ cao đến thấp
                filteredOrderList.sort((order1, order2) -> 
                    Double.compare(order2.getFinalAmount(), order1.getFinalAmount())
                );
                break;
                
            case "price_low":
                // Sắp xếp theo giá từ thấp đến cao
                filteredOrderList.sort((order1, order2) -> 
                    Double.compare(order1.getFinalAmount(), order2.getFinalAmount())
                );
                break;
        }
    }
    
    private void updateNoResultsView() {
        if (filteredOrderList.isEmpty()) {
            orderListView.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            orderListView.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
        }
    }


    private void loadOrdersFromServer() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Order>> call = apiService.getOrders();

        call.enqueue(new Callback<List<Order>>() {            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (!isAdded() || getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    
                    // Reset tìm kiếm để hiển thị tất cả đơn hàng
                    filteredOrderList.clear();
                    filteredOrderList.addAll(orderList);
                    orderAdapter.notifyDataSetChanged();
                    
                    // Cập nhật giao diện dựa trên kết quả
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
}
