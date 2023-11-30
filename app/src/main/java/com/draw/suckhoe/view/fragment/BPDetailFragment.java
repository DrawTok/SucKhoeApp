package com.draw.suckhoe.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.draw.suckhoe.R;
import com.draw.suckhoe.databinding.BpressureDetailFragmentBinding;
import com.draw.suckhoe.factories.ViewModelFactory;
import com.draw.suckhoe.model.BloodPressure;
import com.draw.suckhoe.myInterface.OnClickItemListener;
import com.draw.suckhoe.utils.MyConstants;
import com.draw.suckhoe.view.activity.DetailsActivity;
import com.draw.suckhoe.view.adapter.GenericAdapter;
import com.draw.suckhoe.view.viewModels.BloodPressureViewModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BPDetailFragment extends Fragment implements OnClickItemListener {

    private int countPosText = 0;
    private BpressureDetailFragmentBinding binding;
    private BloodPressureViewModel viewModel;
    private GenericAdapter<BloodPressure> historyBPAdapter;

    List<BloodPressure> listDataBP;
    DetailsActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BpressureDetailFragmentBinding.inflate(inflater, container, false);
        ViewModelFactory factory = new ViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BloodPressureViewModel.class);

        activity = (DetailsActivity) getActivity();

        listDataBP = new ArrayList<>();

        setupUI();
        return binding.getRoot();
    }

    private void setupUI() {
        setDataAdapter();
        setupBarChart();
        setupPieChart();
        observeBloodPressureData();
        setupArrowButtons();
        OnClickAllHistory();
    }

    private void setupPieChart() {
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setCenterTextSize(20f);
        binding.pieChart.setDrawEntryLabels(false);
        binding.pieChart.setHoleRadius(40f);
        binding.pieChart.setTransparentCircleRadius(45f);
        binding.pieChart.setTouchEnabled(false);
        binding.pieChart.getLegend().setEnabled(false);
        binding.pieChart.invalidate();
    }
    private void handlePieChart(List<BloodPressure> listDataBP) {
        int countType1 = 0;
        int countType2 = 0;
        int countType3 = 0;
        int countType4 = 0;
        int countType5 = 0;
        int countType6 = 0;

        ArrayList<Integer> colors = new ArrayList<>();
        for (BloodPressure bp : listDataBP) {
            int type = bp.getType();
            if (type == 1)
                countType1++;
            else if (type == 2)
                countType2++;
            else if (type == 3)
                countType3++;
            else if (type == 4)
                countType4++;
            else if (type == 5)
                countType5++;
            else if (type == 6)
                countType6++;
        }

        ArrayList<PieEntry> pieData = new ArrayList<>();
        if (countType1 != 0) {
            pieData.add(new PieEntry(countType1, getString(R.string.bPressure_low)));
            colors.add(ContextCompat.getColor(requireContext(), R.color.blue_4th));
        }
        if (countType2 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.green));
            pieData.add(new PieEntry(countType2, getString(R.string.bPressure_normal)));
        }
        if (countType3 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.yellow_primary));
            pieData.add(new PieEntry(countType3, getString(R.string.bPressure_high)));
        }
        if (countType4 != 0) {
            colors.add(ContextCompat.getColor(requireContext(), R.color.orange_primary));
            pieData.add(new PieEntry(countType4, getString(R.string.bPressure_stage_1)));
        }
        if (countType5 != 0) {
            pieData.add(new PieEntry(countType5, getString(R.string.bPressure_stage_2)));
            colors.add(ContextCompat.getColor(requireContext(), R.color.orange_secondary));
        }
        if (countType6 != 0) {
            pieData.add(new PieEntry(countType6, getString(R.string.bPressure_stage_3)));
            colors.add(ContextCompat.getColor(requireContext(), R.color.red_primary));
        }

        PieDataSet dataSet = new PieDataSet(pieData, "");

        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        PieData data = new PieData(dataSet);
        binding.pieChart.setData(data);
        binding.pieChart.setCenterText(listDataBP.size() + "\nIn total");
        binding.pieChart.invalidate();
    }


    private void setupArrowButtons() {
        binding.imvArrowLeft.setOnClickListener(v -> {
            countPosText--;
            if (countPosText == -1)
                countPosText = 3;
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
                sys = String.valueOf(listDataBP.stream().mapToInt(BloodPressure::getSystolic).max().orElse(0));
                dias = String.valueOf(listDataBP.stream().mapToInt(BloodPressure::getDiastolic).max().orElse(0));
                pul = String.valueOf(listDataBP.stream().mapToInt(BloodPressure::getPulse).min().orElse(0));
                break;
            case 1:
                int lastIndex = listDataBP.size() - 1;
                sys = String.valueOf(listDataBP.get(lastIndex).getSystolic());
                dias = String.valueOf(listDataBP.get(lastIndex).getDiastolic());
                pul = String.valueOf(listDataBP.get(lastIndex).getPulse());
                break;
            case 2:
                sys = String.valueOf(listDataBP.stream().mapToInt(BloodPressure::getSystolic).min().orElse(0));
                dias = String.valueOf(listDataBP.stream().mapToInt(BloodPressure::getDiastolic).min().orElse(0));
                pul = String.valueOf(listDataBP.stream().mapToInt(BloodPressure::getPulse).min().orElse(0));
                break;
            default:
                countPosText = 0;
                sys = String.valueOf(averageSystolic());
                dias = String.valueOf(averageDiastolic());
                pul = String.valueOf(averagePulse());
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
        binding.barChartBPressure.setScaleMinima(3f, 0f);
        binding.barChartBPressure.setScaleEnabled(false);
        binding.barChartBPressure.getAxisRight().setDrawLabels(false);
        binding.barChartBPressure.getDescription().setEnabled(false);
        binding.barChartBPressure.getLegend().setEnabled(false);
        binding.barChartBPressure.invalidate();
        XAxis xAxis = binding.barChartBPressure.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new ArrayList<>()));
    }

    private void observeBloodPressureData() {
        viewModel.getListDataBPressure();
        viewModel.getBloodPressureListLiveData().observe(getViewLifecycleOwner(), bloodPressureList -> {
            if (bloodPressureList != null) {
                handleDataBarCHart(bloodPressureList);
                listDataBP = bloodPressureList;
                checkPosText();
                handlePieChart(bloodPressureList);
                List<BloodPressure> latestData = new ArrayList<>();
                if (bloodPressureList.size() > 4) {
                    for (int i = bloodPressureList.size() - 1; i >= bloodPressureList.size() - 4; i--) {
                        latestData.add(bloodPressureList.get(i));
                    }
                } else {
                    for (int i = bloodPressureList.size() - 1; i >= 0; i--) {
                        latestData.add(bloodPressureList.get(i));
                    }
                }
                historyBPAdapter.setList(latestData);
            }
        });
    }

    private void handleDataBarCHart(List<BloodPressure> bloodPressureList) {

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
                timeLabels.add(currentTime);
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
            binding.barChartBPressure.setVisibleXRangeMinimum(10f);
            binding.barChartBPressure.notifyDataSetChanged();
            binding.barChartBPressure.invalidate();
        }
    }

    private String handleDateTime(BloodPressure bloodPressure) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM", Locale.US);
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
        historyBPAdapter = new GenericAdapter<>();
        historyBPAdapter.setListener(this);
        binding.recycleHistoryBP.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleHistoryBP.setHasFixedSize(true);
        binding.recycleHistoryBP.setAdapter(historyBPAdapter);
    }

    private int calculateAverage(String field) {
        int sum = 0;
        for (BloodPressure bp : listDataBP) {
            if ("systolic".equals(field))
                sum += bp.getSystolic();
            else if ("diastolic".equals(field))
                sum += bp.getDiastolic();
            else if ("pulse".equals(field))
                sum += bp.getPulse();
        }
        return listDataBP.isEmpty() ? 0 : sum / listDataBP.size();
    }

    private int averageSystolic() {
        return calculateAverage("systolic");
    }

    private int averageDiastolic() {
        return calculateAverage("diastolic");
    }

    private int averagePulse() {
        return calculateAverage("pulse");
    }

    private void OnClickAllHistory() {
        binding.tvHistory.setOnClickListener(v -> {
            if (activity != null) {
                activity.setTitleName("Lịch sử");
                activity.findViewById(R.id.tvAddNewRecord).setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE_HISTORY", MyConstants.BLOOD_PRESSURE_ID);
                HistoryFragment historyFragment = new HistoryFragment();
                historyFragment.setArguments(bundle);
                activity.replaceFragment(historyFragment);
            }
        });
    }

    @Override
    public void onClickItemListener(int pos) {
        BloodPressure bloodPressure = listDataBP.get(pos);
        Bundle bundle = new Bundle();
        bundle.putParcelable("bPressure_info", bloodPressure);
        bundle.putInt("IS_NEW_DATA", 0);
        BPSuccessFragment bpSuccessFragment = new BPSuccessFragment();
        bpSuccessFragment.setArguments(bundle);
        activity.replaceFragment(bpSuccessFragment);
    }
}
