package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.BpressureDetailFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.adapter.HistoryBPAdapter;
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
import java.util.List;
import java.util.Locale;

public class BPDetailFragment extends Fragment {

    private int countPosText = 0;
    private BpressureDetailFragmentBinding binding;
    private BloodPressureViewModel viewModel;
    private HistoryBPAdapter historyBPAdapter;

    private List<BloodPressure> list;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BpressureDetailFragmentBinding.inflate(inflater, container, false);
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodPressureViewModel.class);
        setupUI();
        return binding.getRoot();
    }

    private void setupUI() {
        setupBarChart();
        observeBloodPressureData();
        setupArrowButtons();
        OnClickAllHistory();
        setDataAdapter();
    }

    private void setupArrowButtons() {
        binding.imvArrowLeft.setOnClickListener(v -> {
            countPosText--;
            checkPosText();
        });

        binding.imvArrowRight.setOnClickListener(v -> {
            countPosText++;
            checkPosText();
        });
    }

    private void checkPosText() {
        String[] texts = {"Trung bình", "Gần đây", "Min", "Max"};
        String sys, dias, pul;

        switch (countPosText) {
            case 3:
                sys = String.valueOf(list.stream().mapToInt(BloodPressure::getSystolic).max().orElse(0));
                dias = String.valueOf(list.stream().mapToInt(BloodPressure::getDiastolic).max().orElse(0));
                pul = String.valueOf(list.stream().mapToInt(BloodPressure::getPulse).min().orElse(0));
                break;
            case 1:
                int lastIndex = list.size() - 1;
                sys = String.valueOf(list.get(lastIndex).getSystolic());
                dias = String.valueOf(list.get(lastIndex).getDiastolic());
                pul = String.valueOf(list.get(lastIndex).getPulse());
                break;
            case 2:
                sys = String.valueOf(list.stream().mapToInt(BloodPressure::getSystolic).min().orElse(0));
                dias = String.valueOf(list.stream().mapToInt(BloodPressure::getDiastolic).min().orElse(0));
                pul = String.valueOf(list.stream().mapToInt(BloodPressure::getPulse).min().orElse(0));
                break;
            default:
                countPosText = 0;
                sys = String.valueOf(averageSystolic(list));
                dias = String.valueOf(averageDiastolic(list));
                pul = String.valueOf(averagePulse(list));
                break;
        }

        binding.tvLvSystolic.setText(sys);
        binding.tvLvDiastolic.setText(dias);
        binding.tvLvPulse.setText(pul);
        binding.tvSelection.setText(texts[countPosText]);
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
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new ArrayList<>()));
    }

    private void observeBloodPressureData() {
        viewModel.getListDataBPressure();

        viewModel.getBloodPressureListLiveData().observe(getViewLifecycleOwner(), bloodPressureList -> {
            if (bloodPressureList != null) {
                list = bloodPressureList;
                handleDataBarCHart(bloodPressureList);
                historyBPAdapter.setList(bloodPressureList, 4);
            }
        });
    }

    private void handleDataBarCHart(List<BloodPressure> bloodPressureList)
    {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> timeLabels = new ArrayList<>();
        String previousTime = "";
        for (int i = 0; i < bloodPressureList.size(); i++) {
            BloodPressure bloodPressure = bloodPressureList.get(i);
            float value = (float) bloodPressure.getSystolic();
            barEntries.add(new BarEntry(i, value));
            String currentTime = handleDateTime(bloodPressure);
            if (previousTime.equals(currentTime))
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
            XAxis xAxis = binding.barChartBPressure.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(timeLabels));
            xAxis.setLabelCount(timeLabels.size());
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(timeLabels.size());
            binding.barChartBPressure.notifyDataSetChanged();
            binding.barChartBPressure.invalidate();
        }
    }
    private String handleDateTime(BloodPressure bloodPressure) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd", Locale.US);
        try {
            Date date = inputDateFormat.parse(bloodPressure.getTime());
            if (date != null)
                return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void setDataAdapter() {
        binding.recycleHistoryBP.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleHistoryBP.setHasFixedSize(true);
        historyBPAdapter = new HistoryBPAdapter();
        binding.recycleHistoryBP.setAdapter(historyBPAdapter);
    }

    private int calculateAverage(List<BloodPressure> list, String field) {
        int sum = 0;
        for (BloodPressure bp : list) {
            if ("systolic".equals(field))
                sum += bp.getSystolic();
            else if ("diastolic".equals(field))
                sum += bp.getDiastolic();
            else if ("pulse".equals(field))
                sum += bp.getPulse();
        }
        return list.isEmpty() ? 0 : sum / list.size();
    }

    private int averageSystolic(List<BloodPressure> list) {
        return calculateAverage(list, "systolic");
    }

    private int averageDiastolic(List<BloodPressure> list) {
        return calculateAverage(list, "diastolic");
    }

    private int averagePulse(List<BloodPressure> list) {
        return calculateAverage(list, "pulse");
    }

    private void OnClickAllHistory()
    {
        binding.tvHistory.setOnClickListener(v -> {
            DetailsActivity activity= (DetailsActivity) getActivity();
            if(activity != null) {
                activity.setTitleName("Lịch sử");
                activity.findViewById(R.id.tvAddNewRecord).setVisibility(View.GONE);
                activity.replaceFragment(new HistoryFragment());
            }
        });
    }
}
