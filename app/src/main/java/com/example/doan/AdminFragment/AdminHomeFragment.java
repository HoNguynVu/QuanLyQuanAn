package com.example.doan.AdminFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.text.DecimalFormat;

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

        DecimalFormat formatter = new DecimalFormat("#,###");

        txtTotalOrders.setText("Tổng đơn hàng: " + totalOrders);
        txtTodayRevenue.setText("Doanh thu hôm nay: " + formatter.format(todayRevenue) + " VND");
        txtMonthlyRevenue.setText("Doanh thu theo tháng: " + formatter.format(monthlyRevenue) + " VND");

        // Hiển thị biểu đồ
        displayChart();
    }

    private void displayChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 500000));
        entries.add(new BarEntry(2, 10000000));

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColors(new int[]{Color.parseColor("#03A9F4"), Color.parseColor("#FFC107")}); // xanh + vàng

        BarData barData = new BarData(dataSet);

        // Định dạng số có dấu phẩy cho dữ liệu cột
        ValueFormatter currencyFormatter = new ValueFormatter() {
            private final DecimalFormat mFormat = new DecimalFormat("#,###");
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value) + " VND";
            }
        };
        barData.setValueFormatter(currencyFormatter);

        // Gán dữ liệu vào biểu đồ
        barChart.setData(barData);

        // Tuỳ chỉnh trục Y trái: định dạng số
        barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            private final DecimalFormat mFormat = new DecimalFormat("#,###");
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value);
            }
        });

        // Tuỳ chỉnh thêm
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(1000);

        // Gán nhãn trục X
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"", "Hôm nay", "Tháng này"}));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.invalidate(); // Vẽ lại biểu đồ
    }

}
