package com.example.doan.AdminFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.DatabaseClassResponse.StatisticsResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeFragment extends Fragment {

    private TextView txtTotalOrders, txtTodayRevenue, txtMonthlyRevenue;
    private BarChart barChart;

    public AdminHomeFragment(){
        super(R.layout.fragment_home);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTotalOrders = view.findViewById(R.id.txtTotalOrders);
        txtTodayRevenue = view.findViewById(R.id.txtTodayRevenue);
        txtMonthlyRevenue = view.findViewById(R.id.txtMonthlyRevenue);
        barChart = view.findViewById(R.id.barChart);

        DecimalFormat formatter = new DecimalFormat("#,###");

        // Gọi API lấy thống kê
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        apiService.getStatistics().enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StatisticsResponse stats = response.body();

                    txtTotalOrders.setText("Tổng đơn hàng: " + stats.getTotalOrders());
                    txtTodayRevenue.setText("Doanh thu hôm nay: " + formatter.format(stats.getTodayRevenue()) + " VND");
                    txtMonthlyRevenue.setText("Doanh thu theo tháng: " + formatter.format(stats.getMonthlyRevenue()) + " VND");

                    // Vẽ biểu đồ
                    displayChart(stats.getTodayRevenue(), stats.getMonthlyRevenue());
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                txtTotalOrders.setText("Lỗi kết nối");
                txtTodayRevenue.setText("");
                txtMonthlyRevenue.setText("");
            }
        });
    }

    // Hiển thị biểu đồ
    private void displayChart(double todayRevenue, double monthlyRevenue) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float) todayRevenue));
        entries.add(new BarEntry(1, (float) monthlyRevenue));

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColors(new int[]{Color.parseColor("#03A9F4"), Color.parseColor("#FFC107")});

        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new ValueFormatter() {
            private final DecimalFormat mFormat = new DecimalFormat("#,###");
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value) + " VND";
            }
        });

        barChart.setData(barData);
        barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            private final DecimalFormat mFormat = new DecimalFormat("#,###");
            @Override
            public String getFormattedValue(float value) {
                return mFormat.format(value);
            }
        });
        //Tắt tương tác
        barChart.setTouchEnabled(false);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"Hôm nay", "Tháng này"}));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setTextSize(10f);
        barChart.animateY(1000);
        barChart.invalidate();
    }


}
