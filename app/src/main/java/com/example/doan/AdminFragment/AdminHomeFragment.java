package com.example.doan.AdminFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;

import java.util.ArrayList;

public class AdminHomeFragment extends Fragment {

    private TextView txtTotalOrders, txtTodayRevenue, txtMonthlyRevenue;
    private BarChart barChart;

    public AdminHomeFragment(){
        super(R.layout.fragment_home);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtTotalOrders = view.findViewById(R.id.txtTotalOrders);
        txtTodayRevenue = view.findViewById(R.id.txtTodayRevenue);
        txtMonthlyRevenue = view.findViewById(R.id.txtMonthlyRevenue);
        barChart = view.findViewById(R.id.barChart);

        // Giả lập dữ liệu thống kê
        int totalOrders = 120; // Tổng đơn hàng
        double todayRevenue = 500000; // Doanh thu hôm nay
        double monthlyRevenue = 10000000; // Doanh thu theo tháng

        txtTotalOrders.setText("Tổng đơn hàng: " + totalOrders);
        txtTodayRevenue.setText("Doanh thu hôm nay: " + todayRevenue + " VND");
        txtMonthlyRevenue.setText("Doanh thu theo tháng: " + monthlyRevenue + " VND");

        // Hiển thị biểu đồ
        displayChart();
    }

    private void displayChart() {
        // Dữ liệu biểu đồ
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 500000));  // Doanh thu hôm nay
        entries.add(new BarEntry(2, 10000000)); // Doanh thu theo tháng

        // Tạo BarDataSet
        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));

        // Tạo BarData
        BarData barData = new BarData(dataSet);

        // Cài đặt cho biểu đồ
        barChart.setData(barData);
        barChart.invalidate(); // Cập nhật lại biểu đồ
    }
}
