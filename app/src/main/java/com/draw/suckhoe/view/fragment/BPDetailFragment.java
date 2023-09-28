package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.draw.suckhoe.databinding.BpressureDetailFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.view.viewModels.BloodPressureViewModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BPDetailFragment extends Fragment {
    private BpressureDetailFragmentBinding binding;
    private BloodPressureViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BpressureDetailFragmentBinding.inflate(inflater, container, false);
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodPressureViewModel.class);

        setupBarChart();
        observeBloodPressureData();

        return binding.getRoot();
    }

    private void setupBarChart() {
        BarDataSet barDataSet = new BarDataSet(new ArrayList<>(), "");
        BarData barData = new BarData(barDataSet);
        binding.barChartBPressure.setData(barData);
        binding.barChartBPressure.getAxisRight().setDrawLabels(false);
        binding.barChartBPressure.getDescription().setEnabled(false);
        binding.barChartBPressure.getLegend().setEnabled(false);
        binding.barChartBPressure.invalidate();
        XAxis xAxis = binding.barChartBPressure.getXAxis();
        xAxis.setGranularity(1f); // Đảm bảo rằng mỗi cột x sẽ nằm cách nhau một đơn vị (1)
        xAxis.setDrawLabels(true); // Cho phép hiển thị nhãn trên trục x
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt vị trí của trục x ở dưới biểu đồ
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new ArrayList<>())); // Đặt giá trị ban đầu là một danh sách trống
    }

    private void observeBloodPressureData() {
        viewModel.getListDataBPressure();

        viewModel.getBloodPressureListLiveData().observe(getViewLifecycleOwner(), bloodPressureList -> {
            if (bloodPressureList != null) {
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                ArrayList<String> timeLabels = new ArrayList<>();
                String previousTime = "";
                for (int i = 0; i < bloodPressureList.size(); i++) {
                    BloodPressure bloodPressure = bloodPressureList.get(i);
                    float value = (float) bloodPressure.getSystolic();
                    barEntries.add(new BarEntry(i, value));
                    String currentTime =handleDateTime(bloodPressure);
                    if(previousTime.equals(currentTime))
                        timeLabels.add("");
                    else {
                        previousTime = currentTime;
                        timeLabels.add(handleDateTime(bloodPressure));
                    }

                }

                BarData barData = binding.barChartBPressure.getData();
                if (barData != null) {
                    barData.clearValues();
                    barData.addDataSet(new BarDataSet(barEntries, ""));
                    // Cập nhật giá trị của trục x với danh sách timeLabels
                    XAxis xAxis = binding.barChartBPressure.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(timeLabels));

                    // Cập nhật danh sách nhãn trục x
                    xAxis.setLabelCount(timeLabels.size());
                    xAxis.setAxisMinimum(0);
                    xAxis.setAxisMaximum(timeLabels.size());

                    binding.barChartBPressure.notifyDataSetChanged();
                    binding.barChartBPressure.invalidate();
                }
            }
        });
    }

    private String handleDateTime(BloodPressure bloodPressure) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd", Locale.US);

        try {
            Date date = inputDateFormat.parse(bloodPressure.getTime());
            if(date != null)
                return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
}
