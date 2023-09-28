package com.draw.suckhoe.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draw.suckhoe.databinding.BpressureDetailFragmentBinding;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class BPDetailFragment extends Fragment {
    BpressureDetailFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BpressureDetailFragmentBinding.inflate(inflater, container, false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 50));
        barEntries.add(new BarEntry(1, 70));
        barEntries.add(new BarEntry(2, 90));
        barEntries.add(new BarEntry(3, 110));
        barEntries.add(new BarEntry(4, 130));
        barEntries.add(new BarEntry(5, 150));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Trục tung");
        barDataSet.setColor(Color.BLUE);
        BarData barData = new BarData(barDataSet);
        binding.barChartBPressure.setData(barData);
        XAxis xAxis = binding.barChartBPressure.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
        binding.barChartBPressure.invalidate();
        return binding.getRoot();
    }
}
